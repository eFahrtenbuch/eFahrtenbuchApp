package com.example.efahrtenbuchapp.http;

import java.util.HashMap;

/**
 * Simpler Builder zum erstellen von URLs für den Webservices bzw. die HTTP-Requests
 */
public class UrlBuilder {
    private HashMap<String, String> valuesMap;
    private String url = WebServiceRessources.BASE_URL;
    private String protocol = WebServiceRessources.PROTOCOL;
    private String path ;

    /**
     * Konstruktor
     */
    public UrlBuilder(){
        valuesMap = new HashMap<>();
    }

    /**
     * Fügt ein Wert-Schlüssel-Paar hinzu
     * @param name Name des Paramters
     * @param value Wert des Paramters
     * @return die aktuelle Instanz des Builders
     */
    public UrlBuilder param(String name, String value){
        if(!valuesMap.containsKey(name)){
            valuesMap.put(name, value);
        }
        return this;
    }

    /**
     * Setzt den Sub-Pfad für den Webservices
     * @param path der Pfad Ohne "/" einfach nur bspw. insertFahrt
     * @return die aktuelle Instanz des Builders
     */
    public UrlBuilder path(String path){
        this.path = path;
        return this;
    }

    /**
     * Erstellt die URL anhand des Pfads und der Paramter
     * @return die URL als String
     */
    public String build(){
        StringBuilder sb = new StringBuilder(protocol + url + path + (valuesMap.isEmpty() ? "": "?"));
        valuesMap.entrySet().stream().map(entry -> "&" + entry.getKey() + "=" + entry.getValue()).forEach(sb::append);
        return sb.toString();
    }
}
