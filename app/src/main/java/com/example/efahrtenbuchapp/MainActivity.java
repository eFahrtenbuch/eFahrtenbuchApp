package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.eFahrtenbuch.json.WebServiceRessources;
import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.HttpRequester;
import com.example.efahrtenbuchapp.http.UrlBuilder;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity -> onCreate: ", "started");
        Button btAnmelden = findViewById(R.id.btAnmelden);
        btAnmelden.setOnClickListener(click -> {
            //TODO
            String pw = null;
            try {
                pw = PasswordHelper.getEncryptedPassword(((TextView)findViewById(R.id.tfPasswort)).getText().toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            String username = ((TextView)findViewById(R.id.tfName)).getText().toString();
            String url = new UrlBuilder("http://10.0.2.2:8080//")
                            .path("loginUser")
                            .param("username", username)
                            .param( "hashedPasswort", pw)
                            .build();
            Log.d("MAIN ACTIVITY ON CREATE URL:", url);
            ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Verbinde...",
                    "Einloggen...", true);

            HttpRequester.simpleStringRequest(this, url,
                    (String response) -> {
                        login(response.equals("OK"));
                        dialog.dismiss();
                    },
                    error -> {
                        msg(error.networkResponse == null ? WebServiceRessources.ERROR_NO_CONN_LONG :  "Es ist ein Fehler aufgetreten!", true);
                        dialog.dismiss();
            });
        });
        ((Button)findViewById(R.id.btNeuAccErstellen)).setOnClickListener(onClick -> {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });

        ((TextView)findViewById(R.id.textView3)).setOnClickListener(onClick -> {
           Intent myIntent = new Intent(this, ListViewActivity.class);
            startActivity(myIntent);
            HttpRequester.simpleJsonRequest(this, "http://10.0.2.2:8080/loadFahrtenListe?kennzeichen=B OB 385", jsonResponse -> {
                Log.d("onCreate: ", jsonResponse.toString());
                Toast.makeText(MainActivity.this, jsonResponse.toString(), Toast.LENGTH_SHORT).show();
            }, error -> Log.d("ERROR LISTENER:", error.toString()));

        });
    }
    public void login(boolean success){
        Toast toast = Toast.makeText(this, success ? "Erfolgreich eingeloggt" : "Fehlerhafter Login",Toast.LENGTH_LONG);
        toast.show();
    }
    public void msg(String msg, boolean timeLong){
        Toast.makeText(this, msg, timeLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}
