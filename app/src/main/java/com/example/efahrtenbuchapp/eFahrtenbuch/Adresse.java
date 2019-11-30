package com.example.efahrtenbuchapp.eFahrtenbuch;

import org.json.JSONException;
import org.json.JSONObject;

public class Adresse {
	private int id;
	private String strasse;
	private String hausnummer;
	private String ort;
	private int plz;
	private String zusatz;

	//For JSON Converter
	public Adresse(){}

	public Adresse(String strasse, String hausnummer, String ort, int plz, String zusatz) {
		super();
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.ort = ort;
		this.plz = plz;
		this.zusatz = zusatz;
	}	
	public Adresse(int id, String strasse, String hausnummer, String ort, int plz, String zusatz) {
		super();
		this.id = id;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.ort = ort;
		this.plz = plz;
		this.zusatz = zusatz;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	
	public String getStrasse() {
		return strasse;
	}
	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public void setPlz(int plz) {
		this.plz = plz;
	}
	public String getstrasse() {
		return strasse;
	}
	public String getHausnummer() {
		return hausnummer;
	}
	public String getOrt() {
		return ort;
	}
	public int getPlz() {
		return plz;
	}
	public String getZusatz() {
		return this.zusatz; 
	}
	public void setZusatz(String zusatz) {
		this.zusatz = zusatz;
	}

	@Override
	public String toString(){
		return "Adresse [ID: " + id + " Straße:" + strasse +
				" HausNr: " + hausnummer + " Ort: " + ort +
				" PLZ: " + plz + " Zusatz" + zusatz + "]";
	}
}
