package com.example.efahrtenbuchapp.http.json;

import android.util.Log;

import com.example.efahrtenbuchapp.eFahrtenbuch.Adresse;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONConverter {

    private JSONConverter(){}

    /**
     * Erstellt ein Objekt vom Typ clazz und füllt die Attribute mit den Werten aus dem JSONObject
     * @param clazz der Typ des zu erzeugenden Objektes
     * @param json Daten für die Attribute
     * @return
     */
    public static <T> T createObjectFromJSON(Class<T> clazz, JSONObject json){
        HashMap<String, Object> fieldNamesMap = getFieldNamesAsMap(clazz, json, null);
        return mapToObject(clazz, fieldNamesMap);
    }

    public static <T> T createObjectFromJSON(Class<T> clazz, JSONObject json, String... excludedFields){
        HashMap<String, Object> fieldNamesMap = getFieldNamesAsMap(clazz, json, excludedFields);
        return mapToObject(clazz, fieldNamesMap);
    }

    public static List<JSONObject> toJSONList(JSONArray jsonArray){
        ArrayList<JSONObject> jsonList = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                jsonList.add(new JSONObject(jsonArray.get(i).toString()));
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }
        return jsonList;
    }

    /**
     * Erstellt aus einem jsonArray die entsprechenden Objekte in einer Liste
     * @param clazz Klasse der zu erstellenden Objekte
     * @param jsonArray aus dem werden die Objekte erzeugt
     * @param <T> Typ der zu erzeugenden Objekte
     * @return eine Liste aller Objekte die von dem jsonArray repräsentiert werden
     */
    public static <T> List<T> mapToObjectList(Class<T> clazz, JSONArray jsonArray){
        return toJSONList(jsonArray).stream().map(json -> createObjectFromJSON(clazz, json)).collect(Collectors.toList());
    }

    public static Fahrt createFahrtFromJSON(JSONObject json){
        JSONObject startAdresseJSON;
        JSONObject zielAdresseJSON;
        String[] elementsToRemove = {"startAdresse", "zielAdresse", "fahrtEndeDatum", "fahrtBeginnDatum", "fahrtBeginnZeit", "fahrtEndeZeit"};
        try {
            startAdresseJSON = json.getJSONObject("startAdresse");
            zielAdresseJSON = json.getJSONObject("zielAdresse");
            Adresse start = JSONConverter.createObjectFromJSON(Adresse.class, startAdresseJSON);
            Adresse ziel = JSONConverter.createObjectFromJSON(Adresse.class, zielAdresseJSON);

            Date fahrtBeginnDatum = new Date(json.getLong("fahrtBeginnDatum"));
            Date fahrtEndeDatum = new Date(json.getLong("fahrtEndeDatum"));
            Time fahrtBeginnZeit = new Time(fahrtBeginnDatum.getTime() + json.getLong("fahrtBeginnZeit"));
            Time fahrtEndeZeit = new Time(fahrtBeginnDatum.getTime() + json.getLong("fahrtEndeZeit"));
            Arrays.stream(elementsToRemove).forEach(element -> {
                json.remove(element);
            });

            Fahrt fahrt = JSONConverter.createObjectFromJSON(Fahrt.class, json, elementsToRemove);
            fahrt.setFahrtBeginnZeit(fahrtBeginnZeit);
            fahrt.setFahrtBeginnDatum(fahrtBeginnDatum);
            fahrt.setFahrtEndeZeit(fahrtEndeZeit);
            fahrt.setFahrtEndeDatum(fahrtEndeDatum);
            fahrt.setStartAdresse(start);
            fahrt.setZielAdresse(ziel);
            Log.d("JSONConverter", "createFahrtFromJSON: " + fahrt.toString());
            return fahrt;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static<T> HashMap<String, Object> getFieldNamesAsMap(Class<T> clazz, JSONObject json, String... excludedFields) {
       List<String> fieldNames =  Arrays.asList(clazz.getDeclaredFields()).stream().map(Field::getName).collect(Collectors.toList());
       if(excludedFields != null){
           Arrays.stream(excludedFields).forEach(fieldNames::remove);
       }
        HashMap<String, Object> hm = new HashMap<>();
       for(String fieldName : fieldNames){
           try {
               hm.put(fieldName, json.get(fieldName));
           } catch (JSONException e) {
               e.printStackTrace();
               System.out.println("Attribut " + fieldName + "in Klasse: " + clazz.getName() + " wurde nicht gefunden");
           }
       }
       return hm;
    }

    private static <T> T mapToObject(Class<T> clazz, HashMap<String, Object> hm){
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("Fehler bei der Initialisierung von : '" + clazz.getName() + "' (Kein Zugriff)");
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println("Fehler bei der Initialisierung von : '" + clazz.getName() + "' (InstantiationException)");
            return null;
        }
        for(Map.Entry<String, Object> entry : hm.entrySet())
        {
            try {
                Field field = clazz.getDeclaredField((entry.getKey()));
                if(!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(obj, entry.getValue());
            } catch (IllegalAccessException e) {
                //Sollte eig. nicht vorkommen, da alle Felder vorher auf Accessible gesetzt werden.
                e.printStackTrace();
                System.out.println("Fehler beim setzen von : '" + clazz.getName()+"."+ entry.getValue() + "'  (Kein Zugriff)");
                return null;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                System.out.println("Fehler beim setzen von : '" + clazz.getName()+"."+ entry.getValue() + "'  (Kein Zugriff)");
                return null;
            }
        }
        return obj;
    }
}
