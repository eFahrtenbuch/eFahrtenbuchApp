package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.json.JSONConverter;
import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.SimpleRequest;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity -> onCreate: ", "started");
        Button btAnmelden = findViewById(R.id.btAnmelden);
        btAnmelden.setOnClickListener(click -> {
            //TODO
            String pw = PasswordHelper.getEncryptedPassword(((TextView)findViewById(R.id.tfPasswort)).getText().toString());

            String username = ((TextView)findViewById(R.id.tfName)).getText().toString();
            String url = "http://10.0.2.2:8080//loginUser?username=" + username + "&hashedPasswort=" + pw;

            SimpleRequest.simpleResponse(this, url, (String response) -> login(response.equals("OK")), error -> login(false));
        });
        ((Button)findViewById(R.id.btNeuAccErstellen)).setOnClickListener(onClick -> {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });

        ((TextView)findViewById(R.id.textView3)).setOnClickListener(onClick -> {
           /* Intent myIntent = new Intent(this, ListViewActivity.class);
            startActivity(myIntent);*/
            SimpleRequest.simpleJsonRequest(this, "http://10.0.2.2:8080/loadFahrtenListe?kennzeichen=B OB 385", jsonResponse -> {
                Log.d("onCreate: ", jsonResponse.toString());
                Toast.makeText(MainActivity.this, jsonResponse.toString(), Toast.LENGTH_SHORT).show();
            }, error -> Log.d("ERROR LISTENER:", error.toString()));
        });
    }
    public void login(boolean success){
        Toast toast = Toast.makeText(this, success ? "Gute Login" : "Schlechte Login",Toast.LENGTH_LONG);
        toast.show();
    }
}
