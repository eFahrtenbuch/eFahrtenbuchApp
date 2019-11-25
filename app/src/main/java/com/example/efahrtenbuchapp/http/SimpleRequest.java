package com.example.efahrtenbuchapp.http;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.efahrtenbuchapp.eFahrtenbuch.Auto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

//TODO
/*
    Die Klasse muss überarbeitet werden.
    Es muss zu einem Singleton werden (privater Constructor, zugriff über statische getInstance-Methode)
    Instance eager initialisieren anstatt lazy, damit es keine probleme wegen multithreading o.Ä. gibt.
    Für die klasse eine globale RequestQue schaffen.
    Die aktuellen statischen Methoden zu instanz-methoden ändern und dabei die oben genannte RequestQue schaffen.
    Die Methoden die aktuell vorhanden sind, sollen alle übernommen werden. Ggf. werden noch mehr gebraucht.
 */
public class SimpleRequest {
    public static void simpleResponse(Context context, String url, Response.Listener<String> listener) {
        // ...

        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public static void simpleResponse(Context context, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        //TODO

        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void simpleJsonRequest(Context context, String url, Response.Listener<JSONObject> listener) {
       SimpleRequest.simpleJsonRequest(context, url, listener, null);
    }

    public static void simpleJsonRequest(Context context, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        queue.add(jsonObjectRequest);
    }

    public static void simpleJsonArrayRequest(Context context, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        // ...

        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
}
