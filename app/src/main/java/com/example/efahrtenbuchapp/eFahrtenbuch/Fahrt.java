package com.example.efahrtenbuchapp.eFahrtenbuch;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.example.efahrtenbuchapp.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/** Simples Business Objekt zur Repräsentation von einer Fahrt*/
public class Fahrt implements Comparable<Fahrt>{
	private int id;
	private Date fahrtBeginnDatum = DateUtils.aktuellesDatum();
	private Date fahrtBeginnZeit = DateUtils.aktuellesDatum();
	private Date fahrtEndeDatum = DateUtils.aktuellesDatum();
	private Date fahrtEndeZeit = DateUtils.aktuellesDatum();
	private int adresseStartId;
	private Adresse startAdresse = new Adresse(adresseStartId, "", "", "", 0, "");
	private int adresseZielId;
	private Adresse zielAdresse = new Adresse(adresseZielId, "", "", "", 0, "");
	private String reisezweck;
	private String reiseroute;
	private String besuchtePersonenFirmenBehoerden;
	private double kmFahrtBeginn;

	private double kmFahrtEnde;
	private double kmGeschaeftlich;
	private double kmPrivat;
	private double kmWohnArbeit;
	private double kraftstoffLiter;
	private double kraftstoffBetrag;
	private double literPro100km;
	private double sonstigesBetrag;
	private String sonstigesBeschreibung;
	private String fahrerName;
	private boolean edited;
	
	private String kennzeichen;
	
	// no arg konstruktor
	public Fahrt() {
	}
	// full arg konstruktor
	//'Fahrt(int, java.util.Date, java.util.Date, java.util.Date, java.util.Date, int, int, java.lang.String, java.lang.String, java.lang.String, double, double, double, double, java.lang.String, java.lang.String, boolean, java.lang.String)'
	public Fahrt(int id, Date fahrtBeginnDatum, Date fahrtEndeDatum, Date fahrtBeginnZeit, Date fahrtEndeZeit, int adresseStartId, 
			int adresseZielId, String reisezweck, String reiseroute, String beuchtePersonenFirmenBehoerden, double kmFahrtBeginn, double kmFahrtEnde,
			double kmGeschaeftlich, double kmPrivat, double kmWohnArbeit, double kraftstoffLiter, double kraftstoffBetrag, 
			double literPro100km, double sonstigesBetrag, String sonstigesBezeichnung, String fahrerName, boolean edited, String kennzeichen) {
		super();
		this.id = id;
		this.fahrtBeginnDatum = fahrtBeginnDatum;
		this.fahrtEndeDatum = fahrtEndeDatum;
		this.fahrtBeginnZeit = fahrtBeginnZeit;
		this.fahrtEndeZeit = fahrtEndeZeit;
		this.reisezweck = reisezweck;
		this.reiseroute = reiseroute;
		this.besuchtePersonenFirmenBehoerden = beuchtePersonenFirmenBehoerden;
		this.kmFahrtBeginn = kmFahrtBeginn;
		this.kmFahrtEnde = kmFahrtEnde;
		this.kmGeschaeftlich = kmGeschaeftlich;
		this.kmPrivat = kmPrivat;
		this.kmWohnArbeit = kmWohnArbeit;
		this.kraftstoffLiter = kraftstoffLiter;
		this.kraftstoffBetrag = kraftstoffBetrag;
		this.literPro100km = literPro100km;
		this.sonstigesBetrag = sonstigesBetrag;
		this.sonstigesBeschreibung = sonstigesBezeichnung;
		this.adresseStartId = adresseStartId;
		this.fahrerName = fahrerName;
		this.adresseZielId = adresseZielId;
		this.edited = edited;
		this.kennzeichen = kennzeichen;
	}

	/**
	 * Vergleicht die IDs von Fahrten
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Fahrt o) {
		if(this.id > o.getId()) return 1;
		if(this.id < o.getId()) return -1;
		return 0;
	}

	/**
	 * Gibt die Fahrzeit als String aus
	 * @param datum
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String fahrzeitString(Date datum)
	{
		return String.format("%02d:%02d", datum.getMinutes(), datum.getHours());
	}

	/**
	 * Erstellt JSON Objekt aus dem aktuellen Objekt
	 * @return
	 */
	public JSONObject toJSONObject(){
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("fahrtBeginnDatum", fahrtBeginnDatum);
			json.put("fahrtEndeDatum" , fahrtEndeDatum);
			json.put("fahrtBeginnZeit" , fahrtBeginnZeit);
			json.put("fahrtEndeZeit" , fahrtEndeZeit);
			json.put("reisezweck" , reisezweck);
			json.put("reiseroute" , reiseroute);
			json.put("besuchtePersonenFirmenBehoerden" , besuchtePersonenFirmenBehoerden);
			json.put("kmFahrtBeginn" , kmFahrtBeginn);
			json.put("kmFahrtEnde" , kmFahrtEnde);
			json.put("kmGeschaeftlich" , kmGeschaeftlich);
			json.put("kmPrivat" , kmPrivat);
			json.put("kmWohnArbeit" , kmWohnArbeit);
			json.put("kraftstoffLiter" , kraftstoffLiter);
			json.put("kraftstoffBetrag" , kraftstoffBetrag);
			json.put("literPro100km" , literPro100km);
			json.put("sonstigesBetrag" , sonstigesBetrag);
			json.put("sonstigesBeschreibung" , sonstigesBeschreibung);
			json.put("adresseStartId" , adresseStartId);
			json.put("fahrerName" , fahrerName);
			json.put("adresseZielId" , adresseZielId);
			json.put("edited" , edited);
			json.put("kennzeichen" , kennzeichen);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		return "Fahrt{" +
				"id=" + id +
				", fahrtBeginnDatum=" + fahrtBeginnDatum +
				", fahrtBeginnZeit=" + fahrtBeginnZeit +
				", fahrtEndeDatum=" + fahrtEndeDatum +
				", fahrtEndeZeit=" + fahrtEndeZeit +
				", adresseStartId=" + adresseStartId +
				", startAdresse=" + startAdresse +
				", adresseZielId=" + adresseZielId +
				", zielAdresse=" + zielAdresse +
				", reisezweck='" + reisezweck + '\'' +
				", reiseroute='" + reiseroute + '\'' +
				", besuchtePersonenFirmenBehoerden='" + besuchtePersonenFirmenBehoerden + '\'' +
				", kmFahrtBeginn=" + kmFahrtBeginn +
				", kmFahrtEnde=" + kmFahrtEnde +
				", kmGeschaeftlich=" + kmGeschaeftlich +
				", kmPrivat=" + kmPrivat +
				", kmWohnArbeit=" + kmWohnArbeit +
				", kraftstoffLiter=" + kraftstoffLiter +
				", kraftstoffBetrag=" + kraftstoffBetrag +
				", literPro100km=" + literPro100km +
				", sonstigesBetrag=" + sonstigesBetrag +
				", sonstigesBeschreibung='" + sonstigesBeschreibung + '\'' +
				", fahrerName='" + fahrerName + '\'' +
				", edited=" + edited +
				", kennzeichen='" + kennzeichen + '\'' +
				'}';
	}
	/*************************** Nur noch Getter/Setter ***************************/
	public int getAdresseStartId() {
		return adresseStartId;
	}

	public int getAdresseZielId() {
		return adresseZielId;
	}

	public String getReisezweck() {
		return reisezweck;
	}

	public String getReiseroute() {
		return reiseroute;
	}

	public void setReiseroute(String reiseroute) {
		this.reiseroute = reiseroute;
	}

	public String getBesuchtePersonenFirmenBehoerden() {
		return besuchtePersonenFirmenBehoerden;
	}

	public double getKmFahrtBeginn() {
		return kmFahrtBeginn;
	}

	public double getKmFahrtEnde() {
		return kmFahrtEnde;
	}

	public double getKmGeschaeftlich() {
		return kmGeschaeftlich;
	}

	public double getKmPrivat() {
		return kmPrivat;
	}

	public String getSonstigesBeschreibung() {
		return sonstigesBeschreibung;
	}

	public String getFahrerName() {
		return fahrerName;
	}

	public double getKmWohnArbeit() {
		return kmWohnArbeit;
	}

	public double getKraftstoffLiter() {
		return kraftstoffLiter;
	}

	public double getKraftstoffBetrag() {
		return kraftstoffBetrag;
	}

	public double getLiterPro100km() {
		return literPro100km;
	}

	public double getSonstigesBetrag() {
		return sonstigesBetrag;
	}

	public int getId() {
		return id;
	}

	public Date getFahrtBeginnDatum() {
		return fahrtBeginnDatum;
	}

	public Date getFahrtEndeDatum() {
		return fahrtEndeDatum;
	}

	public Date getFahrtBeginnZeit() {
		return fahrtBeginnZeit;
	}
	public String getFahrtBeginnZeitMinusHour()
	{
		return fahrzeitString(DateUtils.minusHours(this.fahrtBeginnZeit, 1));
	}
	public String getFahrtEndeZeitMinusHour()
	{
		return fahrzeitString(DateUtils.minusHours(this.fahrtBeginnZeit, 1));
	}
	public Date getFahrtEndeZeit() {
		return fahrtEndeZeit;
	}
	
	public Adresse getStartAdresse() {
		return startAdresse;
	}

	public void setStartAdresse(Adresse startAdresse) {
		this.startAdresse = startAdresse;
	}

	public Adresse getZielAdresse() {
		return zielAdresse;
	}

	public void setZielAdresse(Adresse zielAdresse) {
		this.zielAdresse = zielAdresse;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setFahrtBeginnDatum(Date fahrtBeginnDatum) {
		this.fahrtBeginnDatum = fahrtBeginnDatum;
	}

	public void setFahrtEndeDatum(Date fahrtEndeDatum) {
		this.fahrtEndeDatum = fahrtEndeDatum;
	}

	public void setFahrtBeginnZeit(Date fahrtBeginnZeit) {
		this.fahrtBeginnZeit = fahrtBeginnZeit;
	}

	public void setFahrtEndeZeit(Date fahrtEndeZeit) {
		this.fahrtEndeZeit = fahrtEndeZeit;
	}

	public void setAdresseStartId(int adresseStartId) {
		this.adresseStartId = adresseStartId;
	}

	public void setAdresseZielId(int adresseZielId) {
		this.adresseZielId = adresseZielId;
	}

	public void setReisezweck(String reisezweck) {
		this.reisezweck = reisezweck;
	}

	public void setbesuchtePersonenFirmenBehoerden(String besuchtePersonenFirmenBehoerden) {
		this.besuchtePersonenFirmenBehoerden = besuchtePersonenFirmenBehoerden;
	}

	public void setKmFahrtBeginn(double kmFahrtBeginn) {
		this.kmFahrtBeginn = kmFahrtBeginn;
	}

	public void setKmFahrtEnde(double kmFahrtEnde) {
		this.kmFahrtEnde = kmFahrtEnde;
	}

	public void setkmGeschaeftlich(double kmGeschaeftlich) {
		this.kmGeschaeftlich = kmGeschaeftlich;
	}

	public void setKmPrivat(double kmPrivat) {
		this.kmPrivat = kmPrivat;
	}

	public void setKmWohnArbeit(double kmWohnArbeit) {
		this.kmWohnArbeit = kmWohnArbeit;
	}

	public void setKraftstoffLiter(double kraftstoffLiter) {
		this.kraftstoffLiter = kraftstoffLiter;
	}

	public void setKraftstoffBetrag(double kraftstoffBetrag) {
		this.kraftstoffBetrag = kraftstoffBetrag;
	}

	public void setLiterPro100km(double literPro100km) {
		this.literPro100km = literPro100km;
	}

	public void setSonstigesBetrag(double sonstigesBetrag) {
		this.sonstigesBetrag = sonstigesBetrag;
	}

	public void setSonstigesBeschreibung(String sonstigesBeschreibung) {
		this.sonstigesBeschreibung = sonstigesBeschreibung;
	}

	public void setFahrerName(String fahrerName) {
		this.fahrerName = fahrerName;
	}

	public boolean getEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	
	public String getKennzeichen() {
		return kennzeichen;
	}

	public void setKennzeichen(String kennzeichen) {
		this.kennzeichen = kennzeichen;
	}
}
