package com.example.efahrtenbuchapp.eFahrtenbuch.json;

import android.util.Log;

import com.example.efahrtenbuchapp.eFahrtenbuch.Adresse;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONConverter {

    private JSONConverter(){}

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

    private static<T> HashMap<String, Object> getFieldNamesAsMap(Class<T> clazz, JSONObject json) {
        return  getFieldNamesAsMap(clazz, json, null);
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

    /**
     * Creates a Object from the type of clazz and fills the fields with the data from the given json
     * @param clazz the type of the Object that is created
     * @param json data for the fields (the categories need to be named exactly
     * @return
     */
    public static <T> T createObjectFromJSON(Class<T> clazz, JSONObject json){
        HashMap<String, Object> fieldNamesMap = getFieldNamesAsMap(clazz, json);
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

    public static Fahrt createFahrtFromJSON(JSONObject json){
        String TAG = "createFahrtFromJSON: ";
        Log.d(TAG, "createFahrtFromJSON: " + json.toString());
        JSONObject startAdresseJSON;
        JSONObject zielAdresseJSON;
        String[] elementsToRemove = {"startAdresse", "zielAdresse", "fahrtEndeDatum", "fahrtBeginnDatum", "fahrtBeginnZeit", "fahrtEndeZeit"};
        try {
            Log.d(TAG, "ursprüngliche JSON von der ganzen Fahrt:" + json.toString());
            startAdresseJSON = json.getJSONObject("startAdresse");
            Log.d(TAG, "startAdresse to JSON");
            zielAdresseJSON = json.getJSONObject("zielAdresse");
            Log.d(TAG, "zielAdresse to JSON");
            Adresse start = JSONConverter.createObjectFromJSON(Adresse.class, startAdresseJSON);
            Log.d(TAG, "startAdresse to Object");
            Log.d(TAG, "JSON-RESPONSE: " + startAdresseJSON);
            Adresse ziel = JSONConverter.createObjectFromJSON(Adresse.class, zielAdresseJSON);
            Log.d(TAG, "zielAdresse to Object");
            Log.d(TAG, "JSON-RESPONSE: " + zielAdresseJSON);
            json.remove("startAdresse");
            Arrays.stream(elementsToRemove).forEach(element -> {
                Log.d(TAG, "Tries to remove " + element);
                json.remove(element);
                Log.d(TAG, "successfully removed " + element);
            });

            Fahrt fahrt = JSONConverter.createObjectFromJSON(Fahrt.class, json, elementsToRemove);
            Log.d(TAG, "Fahrt to json");
            fahrt.setStartAdresse(start);
            fahrt.setZielAdresse(ziel);

            Log.d(TAG, "Fahrt converted:");
            Log.d(TAG, "Ziel-Adresse: " + fahrt.getZielAdresse());
            Log.d(TAG, "Start-Adresse: " + fahrt.getStartAdresse());
            return fahrt;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
