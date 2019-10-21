package com.example.efahrtenbuchapp.eFahrtenbuch;

public class Adresse {
	private int id;
	private String strasse;
	private String hausnummer;
	private String ort;
	private int PLZ;
	private String zusatz;
	public Adresse(String strasse, String hausnummer, String ort, int PLZ, String zusatz) {
		super();
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.ort = ort;
		this.PLZ = PLZ;
		this.zusatz = zusatz;
	}	
	public Adresse(int id, String strasse, String hausnummer, String ort, int PLZ, String zusatz) {
		super();
		this.id = id;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.ort = ort;
		this.PLZ = PLZ;
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
	public void setPLZ(int pLZ) {
		PLZ = pLZ;
	}
	public void setZusatz(String zusatz) {
		this.zusatz = zusatz;
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
	public int getPLZ() {
		return PLZ;
	}
	public String getZusatz() {
		return this.zusatz; 
	}
}
