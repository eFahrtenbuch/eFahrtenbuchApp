package com.example.efahrtenbuchapp.eFahrtenbuch.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONConverter {

    private JSONConverter(){}

    private static<T> HashMap<String, Object> getFieldNamesAsMap(Class<T> clazz, JSONObject json) {
       List<String> fieldNames =  Arrays.asList(clazz.getDeclaredFields()).stream().map(Field::getName).collect(Collectors.toList());
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

    public static List<JSONObject> toJSONList(JSONObject json){

        Log.d("toJSONList", json.toString());
        System.out.println(json.toString());
        JSONArray jsonArray;
        ArrayList<JSONObject> jsonList = new ArrayList<>();
        try {
            jsonArray = new JSONArray(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
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

}
