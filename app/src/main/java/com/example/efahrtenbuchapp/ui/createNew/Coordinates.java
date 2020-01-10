package com.example.efahrtenbuchapp.ui.createNew;


import com.example.efahrtenbuchapp.reverseGeocoding.AddressGeocoding;

import com.example.efahrtenbuchapp.reverseGeocoding.NominatimReverseGeocodingJAPI;

public class Coordinates {
	
	private NominatimReverseGeocodingJAPI RevGeo = new NominatimReverseGeocodingJAPI();
	private double lat, lon;
	
	public Coordinates(double lan, double lon) {
		super();
		this.lat = lan;
		this.lon = lon;
	}
	
	public AddressGeocoding getAddress(){
		
		return this.getRevGeo().getAdress(lat, lon);
	}
	
	public String getCode(){
		
		return this.getRevGeo().getAdress(lat, lon).getPostcode();
	}
	
	public String getStreet(){
		
		return this.getRevGeo().getAdress(lat, lon).getRoad();
	}
	
	public String getCity(){
		String State = this.getAddress().getState();
		String County = this.getAddress().getCounty();
		return !County.isEmpty() ? County + " " + State : State;
	}
	
	public int getNumber(){
		String[] split = getAddress().toString().split(",");
		try {
			return Integer.parseInt(split[0]);
		} 
		catch (Exception e) {
			return -1;
		}
	}

	public NominatimReverseGeocodingJAPI getRevGeo() {
		return RevGeo;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
}
