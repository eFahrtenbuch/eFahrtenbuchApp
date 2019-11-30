package com.example.efahrtenbuchapp.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/** Diese Klasse bietet ausschließlich statische Methoden zum erstellen von verschiedenen Requests an */
public class HttpRequester {

    private static final QueueManager queueManager = QueueManager.getInstance();

    /** Performs a GET Request on the given URL and expecting a String as response */
    public void simpleStringRequest(Context context, String url, Response.Listener<String> listener) {
        HttpRequester.simpleStringRequest(context, Request.Method.GET, url, listener, VolleyError::printStackTrace);
    }

    /** Performs a GET Request on the given URL and expecting a String as response */
    public static void simpleStringRequest(Context context, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
      HttpRequester.simpleStringRequest(context, Request.Method.GET, url, listener, errorListener);
    }

    /** Performs a Request on the given URL and expecting a String as response */
    public static void simpleStringRequest(Context context, int httpMethod, String url, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        RequestQueue queue = queueManager.getOrCreateQueueForContext(context);
        queue.add(new StringRequest(httpMethod, url, listener, errorListener));
    }

    /** Performs a GET Request on the given URL and expecting a JSONObject as response */
    public static void simpleJsonRequest(Context context, String url, Response.Listener<JSONObject> listener) {
       HttpRequester.simpleJsonRequest(context, url, listener, null);
    }

    /** Performs a GET Request on the given URL and expecting a JSONObject as response */
    public static void simpleJsonRequest(Context context, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
       HttpRequester.simpleJsonRequest(context, Request.Method.GET, url, null, listener, errorListener);
    }

    /** Performs a Request on the given URL and expecting a JSONObject as response */
    public static void simpleJsonRequest(Context context, int httpMethod, String url, JSONObject requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = queueManager.getOrCreateQueueForContext(context);
        queue.add(new JsonObjectRequest(httpMethod, url, requestBody, listener, errorListener));
    }

    /** Performs a GET Request on the given URL and expecting a JSONArray as response */
    public static void simpleJsonArrayRequest(Context context, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        HttpRequester.simpleJsonArrayRequest(context, Request.Method.GET, url, null, listener, errorListener);
    }

    /** Performs a GET Request on the given URL and expecting a JSONArray as response */
    public static void simpleJsonArrayRequest(Context context, int httpMethod, String url, JSONArray requestBody, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = queueManager.getOrCreateQueueForContext(context);
        queue.add(new JsonArrayRequest(httpMethod, url, requestBody, listener, errorListener));
    }
}
