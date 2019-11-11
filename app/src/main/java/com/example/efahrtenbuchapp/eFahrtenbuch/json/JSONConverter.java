package com.example.efahrtenbuchapp.eFahrtenbuch.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONConverter<T> {

    public JSONConverter(){

    }

    private HashMap<String, Object> getFieldNamesAsMap(Class<T> clazz, JSONObject json) {
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

    private T mapToObject(Class<T> clazz, HashMap<String, Object> hm){
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


    public T createFromJSON(Class<T> clazz, JSONObject json){
        JSONConverter<T> converter = new JSONConverter<T>();
        HashMap<String, Object> fieldNamesMap = converter.getFieldNamesAsMap(clazz, json);
        return converter.mapToObject(clazz, fieldNamesMap);
    }

}
