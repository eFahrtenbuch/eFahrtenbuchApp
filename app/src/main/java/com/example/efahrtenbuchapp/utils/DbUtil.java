package com.example.efahrtenbuchapp.utils;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import eFahrtenbuch.Adresse;
import eFahrtenbuch.Auto;
import eFahrtenbuch.Fahrt;
import eFahrtenbuch.User;
import helper.DbHelper;
import helper.PasswordHelper;
/**
 * Diese Klasse k�mmert sich um die ganze Datenbankverbindung.
 * Sie ist nach den Singleton Designpattern designt, was bedeutet, es kann immer nur max. 1 Instanz dieser Klasse geben.
 * @author Niklas
 *
 */
public class DbUtil {

	private static DbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/eFahrt";
	public synchronized static DbUtil getInstance() throws NamingException {
		if (instance == null) {
			instance = new DbUtil();
		}
		
		return instance;
	}
	
	private DbUtil() throws NamingException {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	public boolean isUserAvaible(String userName) {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("id", "user", "benutzername = '" + userName + "'");

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			myRs.next();
			return !myRs.isAfterLast() && myRs.isLast();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close (myConn, myStmt, myRs);
		}
		return false;
	}
	
	public Optional<User> getUserByUserName(String userName) {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("id, benutzername, name, vorname", "user", "benutzername = '" + userName + "'");

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			myRs.next();

			int id = myRs.getInt("id");
			String benutzername = myRs.getString("benutzername");
			String name = myRs.getString("name");
			String vorname = myRs.getString("vorname");
			return Optional.of(new User(id, benutzername, name, vorname));		
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	/**
	 * L�d das verschl�sselte Passwort aus der Tabelle
	 * @return
	 * @throws Exception
	 */
	public String getHashedPasswordForUser(User user) {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("passwort", "user", "id = " + user.getId());

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			myRs.next();

			String pw = myRs.getString("passwort");
			return pw;		
		} catch (Exception e) {
			return null;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	/**
	 * Updated das Password in der Datenbank
	 * @param hashedPassword (muss 64 Zeichen lang sein)
	 * @return
	 */
	public boolean updatePw(User user, String hashedPassword)
	{
		if(user.isValid()) {
			if(hashedPassword.length() != 64) throw new IllegalArgumentException("FEHLER: Passwort muss ein SHA-256 Hash "
					+ "mit einer länge von 64 Zeichen sein");
			Connection myConn = null;
			PreparedStatement myStmt = null;
				try {
					myConn = getConnection();

				String sql = "UPDATE user SET passwort=? WHERE id = " + user.getId();

				myStmt = myConn.prepareStatement(sql);
				myStmt.setString(1, hashedPassword);
				myStmt.execute();

				return true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					close (myConn, myStmt);
				}
		}
		else{
			throw new IllegalArgumentException("Ungültiger Benutzer");
		}
		return false;
	}
	/**
	 * L�dt die Liste an Fahrten.
	 * @return Fahrtenliste
	 */
	public List<Fahrt> loadFahrtenListe(String kennzeichen)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = "SELECT * FROM fahrtenbuch WHERE kennzeichen = '" + kennzeichen + "'";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			ArrayList<Fahrt> fahrtenListe = new ArrayList<Fahrt>();
			while(myRs.next())
			{
				int fahrtId = myRs.getInt("id");
				Date fahrtBeginnDatum = myRs.getDate("datum_fahrt_beginn");
				Date fahrtEndeDatum = myRs.getDate("datum_fahrt_ende");
				Time fahrtBeginnZeit = myRs.getTime("zeit_fahrt_beginn");
				Time fahrtEndeZeit = myRs.getTime("zeit_fahrt_ende");
				int adresseStartId = myRs.getInt("adresse_start");
				int adresseZielId = myRs.getInt("adresse_ziel");
				String reisezweck = myRs.getString("reisezweck");
				String reiseRoute = myRs.getString("reise_route");
				String besuchteFirmen = myRs.getString("besuchte_firmen");
				Double kmFahrtBeginn = myRs.getDouble("km_fahrt_beginn");
				Double kmFahrtende= myRs.getDouble("km_fahrt_ende");
				Double kmPrivat = myRs.getDouble("km_privat");
				Double kmGeschaeftlich = myRs.getDouble("km_geschaeftlich");
				Double kmWohnArbeit = myRs.getDouble("km_wohn_arbeit");
				Double spritLiter = myRs.getDouble("sprit_liter");
				Double betragLiter = myRs.getDouble("betrag_liter");
				Double liter100Km = myRs.getDouble("liter_hundert_km");
				Double sonstigesBetrag = myRs.getDouble("sonstiges_betrag");
				String sosnstigesBezeichnung = myRs.getString("sonstiges_beschreibung");
				String fahrerName = myRs.getString("fahrer_name");
				boolean edited = myRs.getBoolean("edited");
				String kennzeichen2 = myRs.getString("kennzeichen");
				//addresse
				
				Fahrt tempFahrt = new Fahrt(fahrtId, fahrtBeginnDatum, fahrtEndeDatum, fahrtBeginnZeit, fahrtEndeZeit, adresseStartId, adresseZielId,
						reisezweck, reiseRoute, besuchteFirmen, kmFahrtBeginn, kmFahrtende, kmPrivat, kmGeschaeftlich, kmWohnArbeit, 
						spritLiter, betragLiter, liter100Km, sonstigesBetrag, sosnstigesBezeichnung, fahrerName, edited, kennzeichen2);		
				fahrtenListe.add(tempFahrt);
			}
			if(fahrtenListe.isEmpty()) {
				return emptyListFahrt();
			}
			return fahrtenListe;
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return emptyListFahrt();
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	private List<Fahrt> emptyListFahrt()
	{
		List<Fahrt> llist = new ArrayList<Fahrt>();
		String fehlermeldung = "ERROR 101";
		llist.add(new Fahrt(1, new Date(), new Date(), new Date(), new Date(), 0, 0, fehlermeldung, fehlermeldung, fehlermeldung, 0, 0, 0, 0, 0, 0, 0, 0, 0, fehlermeldung, fehlermeldung, true, "n/a"));
		return llist;
	}
	/**
	 * L�d das verschl�sselte Passwort aus der Tabelle
	 * @return
	 * @throws Exception
	 */
	public String loadPW() throws Exception
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.easySelect("passwort");

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			myRs.next();
			
				String pw = myRs.getString("passwort");
			return pw;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	/**
	 * L�dt die Fahrt mit der spezifizierten Adresse
	 * @param id der Fahrt
	 * @return die Fahrt zur id
	 * @throws SQLException
	 */
	public Fahrt loadFahrt(int id) throws SQLException
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("*", "fahrtenbuch", "id = '" + id +"'");
			System.out.println(query);
			
			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			if(myRs.isAfterLast()) return null;
			myRs.next();

			Date fahrtBeginnDatum = myRs.getDate("datum_fahrt_beginn");
			Date fahrtEndeDatum = myRs.getDate("datum_fahrt_ende");
			Time fahrtBeginnZeit = myRs.getTime("zeit_fahrt_beginn");
			Time fahrtEndeZeit = myRs.getTime("zeit_fahrt_ende");
			int adresseStartId = myRs.getInt("adresse_start");
			int adresseZielId = myRs.getInt("adresse_ziel");
			String reisezweck = myRs.getString("reisezweck");
			String reiseRoute = myRs.getString("reise_route");
			String besuchteFirmen = myRs.getString("besuchte_firmen");
			Double kmFahrtBeginn = myRs.getDouble("km_fahrt_beginn");
			Double kmFahrtende= myRs.getDouble("km_fahrt_ende");
			Double kmPrivat = myRs.getDouble("km_privat");
			Double kmGeschaeftlich = myRs.getDouble("km_geschaeftlich");
			Double kmWohnArbeit = myRs.getDouble("km_wohn_arbeit");
			Double spritLiter = myRs.getDouble("sprit_liter");
			Double betragLiter = myRs.getDouble("betrag_liter");
			Double liter100Km = myRs.getDouble("liter_hundert_km");
			Double sonstigesBetrag = myRs.getDouble("sonstiges_betrag");
			String sosnstigesBezeichnung = myRs.getString("sonstiges_beschreibung");
			String fahrerName = myRs.getString("fahrer_name");
			boolean edited = myRs.getBoolean("edited");
			String kennzeichen = myRs.getString("kennzeichen");
			//addresse
			
			
			
			Fahrt lFahrt = new Fahrt(id, fahrtBeginnDatum, fahrtEndeDatum, fahrtBeginnZeit, fahrtEndeZeit, adresseStartId, adresseZielId,
					reisezweck, reiseRoute, besuchteFirmen, kmFahrtBeginn, kmFahrtende, kmPrivat, kmGeschaeftlich, kmWohnArbeit, 
					spritLiter, betragLiter, liter100Km, sonstigesBetrag, sosnstigesBezeichnung, fahrerName, edited, kennzeichen);	
			lFahrt.setStartAdresse(this.loadAdresse(lFahrt.getAdresseStartId()));
			lFahrt.setZielAdresse(this.loadAdresse(lFahrt.getAdresseZielId()));
			
			return lFahrt;
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return null;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	/**
	 * L�dt die spezifizierte Adresse
	 * @param adressid
	 * @return
	 */
	public Adresse loadAdresse(int adressid)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("strasse, hausnr, plz, ort, zusatz", "adresse", "id = " + adressid);// "SELECT km_fahrt_beginn FROM fahrtenbuch WHERE id =" + fahrtid;

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			//Wenn leer, gib null zur�ck
			if(!myRs.next()) {
				return null;
			}
			
			String strasse = myRs.getString("strasse");
			String hausnr = myRs.getString("hausnr");
			int plz = myRs.getInt("plz");
			String ort = myRs.getString("ort");
			String zusatz = myRs.getString("zusatz");
			
			Adresse adresse = new Adresse(strasse, hausnr, ort, plz, zusatz);
			
			return adresse;		
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return null;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	public boolean isAdresseVorhanden(int adressid)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("id", "adresse", "id = " + adressid);

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);

			
				String pw = myRs.getString("passwort");
			return !myRs.isAfterLast();	
		}
		catch (Exception e) {
			return false;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	public void insertFahrt(Fahrt fahrt, Adresse start, Adresse ziel) throws SQLException
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
			myConn = getConnection();
			//INSERT INTO 'fahrtenbuch'('id', 'datum_fahrt_beginn', 'datum_fahrt_ende', 'zeit_fahrt_beginn', 'zeit_fahrt_ende', 'adresse_start', 'adresse_ziel', 'reisezweck', 'reise_route', 'besuchte_firmen', 'km_fahrt_beginn', 'km_fahrt_ende', 'km_privat', 'km_geschaeftlich', 'km_wohn_arbeit', 'sprit_liter', 'betrag_liter', 'liter_hundert_km', 'sonstiges_betrag', 'sonstiges_beschreibung', 'fahrer_name') VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6],[value-7],[value-8],[value-9],[value-10],[value-11],[value-12],[value-13],[value-14],[value-15],[value-16],[value-17],[value-18],[value-19],[value-20])
			String query = "INSERT INTO fahrtenbuch VALUES (null,'"
					+ DateUtils.dateToSqlString(fahrt.getFahrtBeginnDatum())+"','"
					+ DateUtils.dateToSqlString(fahrt.getFahrtEndeDatum())+"','"
					+ DateUtils.timeToSqlString(fahrt.getFahrtBeginnZeit()) +"','"
					+ DateUtils.timeToSqlString(fahrt.getFahrtEndeZeit()) +"'," 
					+ start.getId()+","
					+ ziel.getId()+",'"
					+ fahrt.getReisezweck()+"','"
					+ fahrt.getReiseroute()+"','"
					+ fahrt.getBesuchtePersonenFirmenBehoerden() + "',"
					+ fahrt.getKmFahrtBeginn()+","
					+ fahrt.getKmFahrtEnde()+","
					+ fahrt.getKmPrivat()+","
					+ fahrt.getKmGeschaeftlich()+","
					+ fahrt.getKmWohnArbeit()+","
					+ fahrt.getKraftstoffLiter()+","
					+ fahrt.getKraftstoffBetrag()+","
					+ fahrt.getLiterPro100km()+","
					+ fahrt.getSonstigesBetrag()+",'"
					+ fahrt.getSonstigesBeschreibung()+"','"
					+ fahrt.getFahrerName() + "', false, '" + fahrt.getKennzeichen() + "')";
			myStmt = myConn.createStatement();

			myStmt.executeUpdate(query);
			close (myConn, myStmt, myRs);
	}
	public void editFahrt(Fahrt fahrt, Adresse start, Adresse ziel) throws Exception
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();
			//INSERT INTO 'fahrtenbuch'('id', 'datum_fahrt_beginn', 'datum_fahrt_ende', 'zeit_fahrt_beginn', 'zeit_fahrt_ende', 'adresse_start', 'adresse_ziel', 'reisezweck', 'reise_route', 'besuchte_firmen', 'km_fahrt_beginn', 'km_fahrt_ende', 'km_privat', 'km_geschaeftlich', 'km_wohn_arbeit', 'sprit_liter', 'betrag_liter', 'liter_hundert_km', 'sonstiges_betrag', 'sonstiges_beschreibung', 'fahrer_name') VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6],[value-7],[value-8],[value-9],[value-10],[value-11],[value-12],[value-13],[value-14],[value-15],[value-16],[value-17],[value-18],[value-19],[value-20])
			String query = "INSERT INTO fahrtenbuch VALUES (null,'"
					+ DateUtils.dateToSqlString(fahrt.getFahrtBeginnDatum())+"','"
					+ DateUtils.dateToSqlString(fahrt.getFahrtEndeDatum())+"','"
					+ DateUtils.timeToSqlString(fahrt.getFahrtBeginnZeit()) +"','"
					+ DateUtils.timeToSqlString(fahrt.getFahrtEndeZeit()) +"'," 
					+ start.getId()+","
					+ ziel.getId()+",'"
					+ fahrt.getReisezweck()+"','"
					+ fahrt.getReiseroute()+"','"
					+ fahrt.getBesuchtePersonenFirmenBehoerden() + "',"
					+ fahrt.getKmFahrtBeginn()+","
					+ fahrt.getKmFahrtEnde()+","
					+ fahrt.getKmPrivat()+","
					+ fahrt.getKmGeschaeftlich()+","
					+ fahrt.getKmWohnArbeit()+","
					+ fahrt.getKraftstoffLiter()+","
					+ fahrt.getKraftstoffBetrag()+","
					+ fahrt.getLiterPro100km()+","
					+ fahrt.getSonstigesBetrag()+",'"
					+ fahrt.getSonstigesBeschreibung()+"','"
					+ fahrt.getFahrerName() + "','"
					+ fahrt.getEdited() + "' )";
			myStmt = myConn.createStatement();

			myStmt.executeUpdate(query);
			
			return ;
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return ;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	public int getAdressId(Adresse adresse)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();

			String query = "SELECT id FROM adresse WHERE strasse = '"+adresse.getStrasse()+"' AND hausnr = '" + adresse.getHausnummer() + "' AND plz = " + adresse.getPLZ();
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery(query);
			if(myRs.isAfterLast()) {
				return -1;
			}
			myRs.next();
			return myRs.getInt("id");
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return -1;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	/**
	 * F�gt eine neue Adresse ein falls keine Vorhanden ist
	 * @param adresse
	 * @return gibt die id der Adresse zur�ck oder -1 wenn ein Fehler auftrat
	 * @throws Exception
	 */
	public int insertAdresseifNotAvailable(Adresse adresse) throws Exception
	{
		if(this.loadAdresse(adresse.getId()) == null) {
			Connection myConn = null;
			Statement myStmt = null;
			ResultSet myRs = null;
			try {
				myConn = getConnection();
	
				String query = "INSERT INTO adresse VALUES ( null,'"
					+ adresse.getStrasse()+"','"
					+ adresse.getHausnummer()+"','"
					+ adresse.getPLZ()+"','"
					+ adresse.getOrt()+"','"
					+ adresse.getZusatz()+ "' )";
				myStmt = myConn.createStatement();
	
				myStmt.executeUpdate(query);
				
				return getAdressId(adresse);
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
				return -1;
			}
			finally {
				close (myConn, myStmt, myRs);
			}
		}
		else
		{
			//Wenn die ID bereits in der DB vorhanden ist
			return adresse.getId();
		}
	}
	/**
	 * 
	 * @param fahrtid Id der Fahrt f�r den man der Wert m�chte
	 * @return Gibt den Wert oder wenn keine Verbindung zur DB hergestellt werden kann -1 zur�ck
	 */
	public double loadKmFahrtBeginn(int fahrtid)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("km_fahrt_beginn", "fahrtenbuch", "id = " + fahrtid);// "SELECT km_fahrt_beginn FROM fahrtenbuch WHERE id =" + fahrtid;

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			myRs.next();
			
			double km_fahrt_beginn = myRs.getDouble(1);

			return km_fahrt_beginn;		
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return -1;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	/**
	 * 
	 * @param fahrtid Id der Fahrt f�r den man der Wert m�chte
	 * @return Gibt den Wert oder wenn keine Verbindung zur DB hergestellt werden kann -1 zur�ck
	 * Die  ** FEHLERmeldung wird sortiert nach Reihenfolge im Sourcecode immer eins h�he gez�hlt
	 */
	public double loadKmFahrtEnde(int fahrtid)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();

			String query = DbHelper.selectColumnsWhere("km_fahrt_ende", "fahrtenbuch", "id = " + fahrtid);// "SELECT km_fahrt_beginn FROM fahrtenbuch WHERE id =" + fahrtid;

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(query);
			myRs.next();
			
			double km_fahrt_ende = myRs.getDouble(1);

			return km_fahrt_ende;		
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return -1;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void setFahrtEdited(int fahrtid) throws Exception{
		if(fahrtid < 0) throw new IllegalArgumentException("Fahrtids m�ssen >0 sein!");
		
		Connection myConn = null;
		Statement myStmt = null;
			
		myConn = getConnection();
		String sql = "UPDATE fahrtenbuch SET edited=1 WHERE id =" + fahrtid;
		myStmt = myConn.createStatement();
		myStmt.executeUpdate(sql);
		System.out.println(sql);
		close(myConn, myStmt);
	}	
	
	public void registerUser(String benutzername, String password, String vorname, String name) throws NoSuchAlgorithmException, SQLException {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		myConn = getConnection();
		String query = "INSERT INTO user VALUES (null,'"
				+ benutzername + "','"
				+ PasswordHelper.getEncryptedPassword(password) + "','"
				+ vorname + "','" 
				+ name + "')";
		myStmt = myConn.createStatement();
		
		myStmt.executeUpdate(query);
		close (myConn, myStmt, myRs);
	}
	
	public List<Auto> getAutos() {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();
			String query = "SELECT Kennzeichen, Modell, kmStand, passwort from autos";
			myStmt = myConn.createStatement();
			ResultSet rslt = myStmt.executeQuery(query);

			ArrayList<Auto> autoListe = new ArrayList<>();
			while(rslt.next()) {
				autoListe.add(createAutoFromResultSet(rslt));
			}
			
			return autoListe;
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public List<Auto> getAutos(User user) {
		Objects.nonNull(user);
		Objects.nonNull(user.getId());
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		List<String> kennzeichenListe = new ArrayList<>();
		try {
			myConn = getConnection();
			String query = "SELECT Kennzeichen from autouserverbindung WHERE userid = " + user.getId();
			myStmt = myConn.createStatement();
			ResultSet rslt = myStmt.executeQuery(query);

			while(rslt.next()) {
				kennzeichenListe.add(rslt.getString("kennzeichen"));
			}
			kennzeichenListe = kennzeichenListe.stream().filter(DbUtil.getInstance()::isValidAuto).collect(Collectors.toList());
			} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} catch (NamingException e) {
				e.printStackTrace();
				return Collections.emptyList();
			}
		finally {
			close (myConn, myStmt, myRs);
		}
		return kennzeichenListe.stream().map(new KennzeichenListeToAutoListeFunction()).collect(Collectors.toList());
	}
	
	public List<String> getAutosKennzeichen(User user) {
		return getAutosKennzeichen(user, null);
	}
	
	public List<String> getAutosKennzeichen(User user, Predicate<String> validation) {
		Objects.nonNull(user);
		Objects.nonNull(user.getId());
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		List<String> kennzeichenListe = new ArrayList<>();
		try {
			myConn = getConnection();
			String query = "SELECT Kennzeichen from autouserverbindung WHERE userid = " + user.getId();
			myStmt = myConn.createStatement();
			ResultSet rslt = myStmt.executeQuery(query);

			while(rslt.next()) {
					String kennzeichenString = rslt.getString("kennzeichen");
					if(validation != null && validation.test(kennzeichenString)) {
						kennzeichenListe.add(kennzeichenString);
				}
			}
			return kennzeichenListe;
			} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} 
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	
	public boolean isValidAuto(String kennzeichen) {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();
			String query = "SELECT Kennzeichen, Modell, kmStand, passwort from autos WHERE kennzeichen = '" + kennzeichen + "'";
			myStmt = myConn.createStatement();
			ResultSet rslt = myStmt.executeQuery(query);

			rslt.next();
			return DbUtil.isResultSetAtLastEntry(rslt) && 
					StringUtils.notBlank(rslt.getString("kennzeichen")) && 
					StringUtils.notBlank(rslt.getString("modell")) && 
					StringUtils.notBlank(rslt.getString("passwort")) && 
					rslt.getInt("kmStand") != 0;
			} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	private final class KennzeichenListeToAutoListeFunction implements Function<String, Auto> {
		@Override
		public Auto apply(String kennzeichen) {
			try {
				Auto auto = DbUtil.getInstance().getAuto(kennzeichen).orElse(null);
				return auto;
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public Optional<Auto> getAuto(String kennzeichen) {
		Objects.nonNull(kennzeichen);
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();
			String query = "SELECT * from autos WHERE kennzeichen = '" + kennzeichen + "'";
			myStmt = myConn.createStatement();
			ResultSet rslt = myStmt.executeQuery(query);
			rslt.next();
			return Optional.of(createAutoFromResultSet(rslt));
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	private static Auto createAutoFromResultSet(ResultSet rslt) throws SQLException {
		return new Auto(rslt.getString("Kennzeichen"), rslt.getString("Modell"), rslt.getString("passwort"), rslt.getInt("kmStand"));
	}
	
	public void addAutoToUser(User user, Auto auto) {
		Objects.requireNonNull(user, "Der User darf nicht NULL sein");
		Objects.requireNonNull(auto, "Das Auto darf nicht NULL sein");
		addAutoToUser(user.getId(), auto.getKennzeichen());
	}
	
	public void addAutoToUser(int userid, String kennzeichen) {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();
			String query = "INSERT INTO autouserverbindung VALUES ('" + kennzeichen + "',"
					+ userid + ")";
			myStmt = myConn.createStatement();
			myStmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void removeAutoFromUser(int userid, String kennzeichen) {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = getConnection();
			String query = "DELETE FROM autouserverbindung WHERE kennzeichen = '" + kennzeichen + "'";
			myStmt = myConn.createStatement();
			myStmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	/***************************HELPER METHODS***************************/
	
	private Connection getConnection() throws SQLException {
		Connection theConn = dataSource.getConnection();
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection connection, Statement statement, ResultSet resultSet) {
		try {
			if (resultSet != null) 	{resultSet.close();}
			if (statement != null) 	{statement.close();}
			if (connection != null) {connection.close();}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	/**
	 * Checks if the ResultSet only has one entry
	 * You need to call resultSet.next(); first
	 * @param resultSet
	 * @return
	 */
	private static boolean isResultSetAtLastEntry(ResultSet resultSet) {
		try {
			return resultSet.isFirst() && resultSet.isLast();
		} catch (SQLException e) {
			return false;
		}
	}
}
