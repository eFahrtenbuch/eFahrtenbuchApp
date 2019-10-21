package com.example.efahrtenbuchapp.eFahrtenbuch;

public class User implements Cloneable{
	private int id;
	//Max L�nge 64 Zeichen
	private String benutzername;
	//Vor- und Nachname aus der DB max. L�nge 64 Zeichen
	private String name;
	private String vorname;
	public User(int id, String benutzername, String name, String vorname) {
		super();
		this.id = id;
		this.benutzername = benutzername;
		this.name = name;
		this.vorname = vorname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBenutzername() {
		return benutzername;
	}
	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	/**
	 * @return Prüft ob Nicht Null und eine gültige ID & Benutzername vorhanden sind.
	 */
	public boolean isValid() {
		return this != null && this.id >= 0 && !this.benutzername.isEmpty();
	}
	
	@Override
	public String toString() {
		return "Nutzer: " + benutzername +" [" + this.vorname + "," + this.name + "] ID: " + id; 
	}
	
	@Override
	public User clone() {
		return new User(this.id, this.benutzername, this.name, this.vorname);
	}
}
