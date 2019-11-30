package com.example.efahrtenbuchapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListenAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.json.JSONConverter;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.http.SimpleRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        ListView lv = findViewById(R.id.listviewid);
        SimpleRequest.simpleJsonRequest(this, "http://10.0.2.2:8080/loadFahrtenListe?kennzeichen=B OB 385", jsonResponse -> {
            Log.d("onCreate: ", jsonResponse.toString());
            List<FahrtListAdapter> list = JSONConverter.toJSONList(jsonResponse).stream()//
                    .map(json -> (Fahrt) JSONConverter.createObjectFromJSON(Fahrt.class, json))//
                    .map(fahrt -> new FahrtListAdapter(fahrt))
                    .collect(Collectors.toList());
            refreshList(list);
        });

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