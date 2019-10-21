package com.example.efahrtenbuchapp.eFahrtenbuch;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import utils.DateUtils;

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
	
	@Override
	public int compareTo(Fahrt o) {
		if(this.id > o.getId()) return 1;
		if(this.id < o.getId()) return -1;
		return 0;
	}

	
	@SuppressWarnings("deprecation")
	public String getFahrzeit() {

		Date ld1 = new Date(this.fahrtBeginnDatum.getTime() + this.fahrtBeginnZeit.getTime());
		Date ld2 = new Date(this.fahrtEndeDatum.getTime() + this.fahrtEndeZeit.getTime());
		Date ergebnis = new Date(DateUtils.getDateDiff(ld1, ld2, TimeUnit.MILLISECONDS) - 1000*60*60);
		int tage = (int) ergebnis.getTime() / 1000 / 60 / 60 / 24;
		if(tage > 0)
		{
			return String.format("Tage: %d; %02d:%02d H",  tage, ergebnis.getHours(), ergebnis.getMinutes());
		}
		return String.format("%02d:%02d H", ergebnis.getHours(), ergebnis.getMinutes());
	}

	@SuppressWarnings("deprecation")
	private String fahrzeitString(Date datum)
	{
		int minuten = datum.getMinutes();
		int stunden = datum.getHours();
		return String.format("%02d:%02d", stunden, minuten);
	}
	
	/*************************** Nur noch Getter/Setter ***************************/
	public int getAdresseStartId() {
		return adresseStartId;
	}

	public boolean isInDateRange(Date from, Date to)
	{
		if(fahrtBeginnDatum.equals(from) || fahrtBeginnDatum.equals(to) || (fahrtBeginnDatum.before(to) && fahrtBeginnDatum.after(from)))
		{
			return true;
		}
		return false;
	}
	
	public boolean isInIdRange(int from, int to)
	{
		if(this.id >= from && this.id <= to) {return true;}
		return false;
	}
	public boolean isFahrerOrEmpty(String fahrer)
	{
		if(this.fahrerName.equals(fahrer) || fahrer.equals("") || fahrer.equals("null")) {return true;}
		return false;
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
