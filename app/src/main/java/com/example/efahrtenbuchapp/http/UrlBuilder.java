package com.example.efahrtenbuchapp.http;

import java.util.HashMap;

public class UrlBuilder {
    private HashMap<String, String> valuesMap;
    private String url = WebServiceRessources.BASE_URL;
    private String protocol = WebServiceRessources.PROTOCOL;
    private String path ;


    public UrlBuilder(){
        valuesMap = new HashMap<>();
    }
    public UrlBuilder(String url){
        this();
        this.url = url;
    }

    public UrlBuilder param(String name, String value){
        if(!valuesMap.containsKey(name)){
            valuesMap.put(name, value);
        }
        return this;
    }

    /**
     * Ohne / einfach nur bspw. insertFahrt
     * @param path
     * @return
     */
    public UrlBuilder path(String path){
        this.path = path;
        return this;
    }
    public String build(){
        StringBuilder sb = new StringBuilder(protocol + url + path + "?");
        valuesMap.entrySet().stream().map(entry -> "&" + entry.getKey() + "=" + entry.getValue()).forEach(sb::append);
        return sb.toString();
    }
}
