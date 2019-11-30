package com.example.efahrtenbuchapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListenAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.json.JSONConverter;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.http.HttpRequester;
import com.example.efahrtenbuchapp.http.UrlBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        ListView lv = findViewById(R.id.listviewid);
        HttpRequester.simpleJsonArrayRequest(this, "http://10.0.2.2:8080/loadFahrtenListe?kennzeichen=B OB 385", jsonResponse -> {
            Log.d("onCreate: ", jsonResponse.toString());
            List<FahrtListAdapter> list = JSONConverter.toJSONList(jsonResponse).stream()//
                    .map(json -> {
                        Log.d("SEND JSON IN REQUEST BODY: ", json.toString());
                        //HttpRequester.simpleJsonRequest(ListViewActivity.this, Request.Method.POST, "http://10.0.2.2:8080/insertFahrt", json, null, null);
                        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

                        try {

                            HttpPost request = new HttpPost("http://10.0.2.2:8080/insertFahrt");
                            StringEntity params =new StringEntity(json.toString());
                            request.addHeader("content-type", "application/x-www-form-urlencoded");
                            request.setEntity(params);
                            HttpResponse response = httpClient.execute(request);

                            //handle response here...

                        }catch (Exception ex) {

                            //handle exception here

                        }
                        return (Fahrt) JSONConverter.createFahrtFromJSON(json);
                    })//
                    .map(fahrt -> new FahrtListAdapter(fahrt))
                    .collect(Collectors.toList());
            refreshList(list);
        }, null);

        FahrtListenAdapter adapter = new FahrtListenAdapter(this, R.layout.listview, new ArrayList());
        lv.setAdapter(adapter);
    }

    public void message(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void refreshList(List<FahrtListAdapter> list){
        ListView lv = findViewById(R.id.listviewid);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
    }
}