package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import com.example.efahrtenbuchapp.http.SimpleRequest;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btAnmelden = findViewById(R.id.btAnmelden);

        btAnmelden.setOnClickListener(click -> {
            //TODO
            SimpleRequest.simpleResponse(this, "http://10.0.2.2:8080/getAutoByKennzeichen?kennzeichen=B%20OB%20385", (TextView) findViewById(R.id.tfName));
        });
    }
}
