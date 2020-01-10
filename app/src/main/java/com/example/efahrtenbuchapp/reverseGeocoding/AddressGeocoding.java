package com.example.efahrtenbuchapp.reverseGeocoding;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import org.json.JSONException;
import org.json.JSONObject;

public class AddressGeocoding {
    private int lod = -1;
    private long osm_id = -1L;
    private String osm_type = "";
    private String country_code = "";
    private String country = "";
    private String postcode = "";
    private String state = "";
    private String county = "";
    private String city = "";
    private String suburb = "";
    private String road = "";
    private String display_name = "";

    public AddressGeocoding(String json, int lod) {
        try {
            JSONObject jObject = new JSONObject(json);
            if (jObject.has("error")) {
                System.err.println(jObject.get("error"));
                return;
            }

            this.osm_id = jObject.getLong("osm_id");
            this.osm_type = jObject.getString("osm_type");
            this.display_name = jObject.getString("display_name");
            JSONObject addressObject = jObject.getJSONObject("address");
            if (addressObject.has("country_code")) {
                this.country_code = addressObject.getString("country_code");
            }

            if (addressObject.has("country")) {
                this.country = addressObject.getString("country");
            }

            if (addressObject.has("postcode")) {
                this.postcode = addressObject.getString("postcode");
            }

            if (addressObject.has("state")) {
                this.state = addressObject.getString("state");
            }

            if (addressObject.has("county")) {
                this.county = addressObject.getString("county");
            }

            if (addressObject.has("city")) {
                this.city = addressObject.getString("city");
            }

            if (addressObject.has("suburb")) {
                this.suburb = addressObject.getString("suburb");
            }

            if (addressObject.has("road")) {
                this.road = addressObject.getString("road");
            }

            this.lod = lod;
        } catch (JSONException var5) {
            System.err.println("Can't parse JSON string");
            var5.printStackTrace();
        }

    }

    public long getOsmId() {
        return this.osm_id;
    }

    public String getOsmType() {
        return this.osm_type;
    }

    public int getLod() {
        return this.lod;
    }

    public String getCountryCode() {
        return this.country_code;
    }

    public String getCountry() {
        return this.country;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public String getState() {
        return this.state;
    }

    public String getCounty() {
        return this.county;
    }

    public String getCity() {
        return this.city;
    }

    public String getSuburb() {
        return this.suburb;
    }

    public String getRoad() {
        return this.road;
    }

    public String getDisplayName() {
        return this.display_name;
    }

    public String toString() {
        return this.display_name;
    }
}
