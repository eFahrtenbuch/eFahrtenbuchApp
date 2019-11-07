package com.example.efahrtenbuchapp.http;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.efahrtenbuchapp.eFahrtenbuch.Auto;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;


public class SimpleRequest {
    public static void simpleResponse(Context context, String url, TextView textView) {
        // ...

        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);//json.toString());
                        System.out.println(response);
                        Log.d(TAG, "onResponse: '" + response + "'");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
