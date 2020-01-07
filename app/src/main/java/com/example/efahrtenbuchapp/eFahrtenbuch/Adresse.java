package com.example.efahrtenbuchapp.eFahrtenbuch;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.example.efahrtenbuchapp.http.HttpRequester;
import com.example.efahrtenbuchapp.http.UrlBuilder;

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

	public void speichere(Context context, Response.Listener<String> listener, Response.ErrorListener errorListener){
		String url = new UrlBuilder().path("insertAdresseifNotAvailable")
				.param("strasse", strasse)
				.param("hausnr", hausnummer)
				.param("ort", ort)
				.param("PLZ", Integer.toString(plz))
				.param("zusatz", zusatz).build();
		Log.d("Adresse: ", "speichere: URL = " + url);
		HttpRequester.simpleStringRequest(context, url, listener, errorListener);
	}
}
