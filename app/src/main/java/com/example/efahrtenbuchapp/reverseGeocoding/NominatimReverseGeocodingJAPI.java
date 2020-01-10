package com.example.efahrtenbuchapp.reverseGeocoding;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class NominatimReverseGeocodingJAPI {
    private final String NominatimInstance = "https://nominatim.openstreetmap.org";
    private int zoomLevel = 18;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("use -help for instructions");
        } else if (args.length < 2) {
            if (args[0].equals("-help")) {
                System.out.println("Mandatory parameters:");
                System.out.println("   -lat [latitude]");
                System.out.println("   -lon [longitude]");
                System.out.println("\nOptional parameters:");
                System.out.println("   -zoom [0-18] | from 0 (country) to 18 (street address), default 18");
                System.out.println("   -osmid       | show also osm id and osm type of the address");
                System.out.println("\nThis page:");
                System.out.println("   -help");
            } else {
                System.err.println("invalid parameters, use -help for instructions");
            }
        } else {
            boolean latSet = false;
            boolean lonSet = false;
            boolean osm = false;
            double lat = -200.0D;
            double lon = -200.0D;
            int zoom = 18;

            for(int i = 0; i < args.length; ++i) {
                if (args[i].equals("-lat")) {
                    try {
                        lat = Double.parseDouble(args[i + 1]);
                    } catch (NumberFormatException var13) {
                        System.out.println("Invalid latitude");
                        return;
                    }

                    latSet = true;
                    ++i;
                } else if (args[i].equals("-lon")) {
                    try {
                        lon = Double.parseDouble(args[i + 1]);
                    } catch (NumberFormatException var12) {
                        System.out.println("Invalid longitude");
                        return;
                    }

                    lonSet = true;
                    ++i;
                } else if (args[i].equals("-zoom")) {
                    try {
                        zoom = Integer.parseInt(args[i + 1]);
                    } catch (NumberFormatException var11) {
                        System.out.println("Invalid zoom");
                        return;
                    }

                    ++i;
                } else {
                    if (!args[i].equals("-osm")) {
                        System.err.println("invalid parameters, use -help for instructions");
                        return;
                    }

                    osm = true;
                }
            }

            if (latSet && lonSet) {
                NominatimReverseGeocodingJAPI nominatim = new NominatimReverseGeocodingJAPI(zoom);
                AddressGeocoding address = nominatim.getAdress(lat, lon);
                System.out.println(address);
                if (osm) {
                    System.out.print("OSM type: " + address.getOsmType() + ", OSM id: " + address.getOsmId());
                }
            } else {
                System.err.println("please specifiy -lat and -lon, use -help for instructions");
            }
        }

    }

    public NominatimReverseGeocodingJAPI() {
    }

    public NominatimReverseGeocodingJAPI(int zoomLevel) {
        if (zoomLevel < 0 || zoomLevel > 18) {
            System.err.println("invalid zoom level, using default value");
            zoomLevel = 18;
        }

        this.zoomLevel = zoomLevel;
    }

    public AddressGeocoding getAdress(double lat, double lon) {
        AddressGeocoding result = null;
        String urlString = "https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&lat=" + String.valueOf(lat) + "&lon=" + lon + "&zoom=" + this.zoomLevel;

        try {
            result = new AddressGeocoding(this.getJSON(urlString), this.zoomLevel);
        } catch (IOException var8) {
            System.err.println("Can't connect to server.");
            var8.printStackTrace();
        }

        return result;
    }

    private String getJSON(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.addRequestProperty("User-Agent", "Mozilla/4.76");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder result = new StringBuilder();

        String text;
        while((text = in.readLine()) != null) {
            result.append(text);
        }

        in.close();
        return result.toString();
    }
}
