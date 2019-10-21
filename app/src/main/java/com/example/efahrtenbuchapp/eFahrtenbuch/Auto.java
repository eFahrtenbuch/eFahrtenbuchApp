package com.example.efahrtenbuchapp.eFahrtenbuch;

public class Auto {
	private String kennzeichen;
	private String modell;
	private String passwort;
	private int kmStand;
	
	public Auto(String kennzeichen, String modell, String passwort, int kmStand) {
		super();
		this.kennzeichen = kennzeichen;
		this.modell = modell;
		this.passwort = passwort;
		this.kmStand = kmStand;
	}
	
	//Getter und Setter//
	public String getKennzeichen() {
		return kennzeichen;
	}
	public void setKennzeichen(String kennzeichen) {
		this.kennzeichen = kennzeichen;
	}
	public String getModell() {
		return modell;
	}
	public void setModell(String modell) {
		this.modell = modell;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	public int getKmStand() {
		return kmStand;
	}
	public void setKmStand(int kmStand) {
		this.kmStand = kmStand;
	}
	@Override
	public String toString() {
		return kennzeichen + "(" + modell + ")";
	}
	@Override
	public boolean equals(Object obj) {
		if(this == null ^ obj == null) {
			return false;
		}
		return this.getKennzeichen().equals(((Auto)obj).getKennzeichen());
	}
}
