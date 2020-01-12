package com.example.efahrtenbuchapp.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;


/** Diese Klasse bietet ausschließlich statische Methoden zum erstellen von verschiedenen Requests an */
public class HttpRequester {

    private static final QueueManager queueManager = QueueManager.getInstance();

    /**
     * Simpler HTTP-Request (Request-Methode: GET)
     * @param context der Context in dem der Request ausgeführt werden soll
     * @param url die URL für den Request
     * @param listener asynchroner Listener für die Response
     * @param errorListener asynchroner Fehler-Listener falls ein Fehler aufgetreten ist.
     */
    public static void simpleStringRequest(Context context, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
      HttpRequester.simpleStringRequest(context, Request.Method.GET, url, listener, errorListener);
    }

    /**
     * Siehe #simpleStringRequest
     * Mit der Erweiterung das die Request-Methode bestimmt werden kann.
     * @param context
     * @param httpMethod
     * @param url
     * @param listener
     * @param errorListener
     */
    public static void simpleStringRequest(Context context, int httpMethod, String url, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        RequestQueue queue = queueManager.getOrCreateQueueForContext(context);
        queue.add(new StringRequest(httpMethod, url, listener, errorListener));
    }

    /**
     * Simpler HTTP-Request der eine Response in Form eines JSONObjects erwartet (Request-Methode: GET)
     * @param context der Context in dem der Request ausgeführt werden soll
     * @param url die URL für den Request
     * @param listener asynchroner Listener für die Response
     * @param errorListener asynchroner Fehler-Listener falls ein Fehler aufgetreten ist.
     */
    public static void simpleJsonRequest(Context context, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
       HttpRequester.simpleJsonRequest(context, Request.Method.GET, url, null, listener, errorListener);
    }

    /** Siehe oben */
    public static void simpleJsonRequest(Context context, int httpMethod, String url, JSONObject requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = queueManager.getOrCreateQueueForContext(context);
        queue.add(new JsonObjectRequest(httpMethod, url, requestBody, listener, errorListener));
    }

    /**
     * Simpler HTTP-Request der eine Response in Form eines JSONArrays erwartet (Request-Methode: GET)
     * @param context der Context in dem der Request ausgeführt werden soll
     * @param url die URL für den Request
     * @param listener asynchroner Listener für die Response
     * @param errorListener asynchroner Fehler-Listener falls ein Fehler aufgetreten ist.
     */
    public static void simpleJsonArrayRequest(Context context, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        HttpRequester.simpleJsonArrayRequest(context, Request.Method.GET, url, null, listener, errorListener);
    }

    /** Siehe Oben */
    public static void simpleJsonArrayRequest(Context context, int httpMethod, String url, JSONArray requestBody, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = queueManager.getOrCreateQueueForContext(context);
        queue.add(new JsonArrayRequest(httpMethod, url, requestBody, listener, errorListener));
    }
}
