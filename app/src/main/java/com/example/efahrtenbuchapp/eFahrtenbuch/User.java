package com.example.efahrtenbuchapp.eFahrtenbuch;

import com.example.efahrtenbuchapp.http.json.JSONConverter;

import org.json.JSONObject;

public class User implements Cloneable {

	private int id;
	//Max Länge 64 Zeichen
	private String benutzername;
	//Vor- und Nachname aus der DB max. länge 64 Zeichen
	private String name;
	private String vorname;

	/**
	 * Wird für JSONConverter gebraucht
	 */
	public User(){}

	/**
	 * Konstruktor
	 * @param id
	 * @param benutzername
	 * @param name
	 * @param vorname
	 */
	public User(int id, String benutzername, String name, String vorname) {
		super();
		this.id = id;
		this.benutzername = benutzername;
		this.name = name;
		this.vorname = vorname;
	}


	// GETTER & SETTER
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
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVorname() {
		return this.vorname;
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

	public static User createFromJson(JSONObject json) {
		return JSONConverter.createObjectFromJSON(User.class, json);
	}
}
