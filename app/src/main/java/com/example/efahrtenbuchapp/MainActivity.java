package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.HttpRequester;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Log.d("MainActivity -> onCreate: ", "started");
        Button btAnmelden = findViewById(R.id.btAnmelden);

        btAnmelden.setOnClickListener(click -> {
            //TODO
            ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                    "Verbinde...", true);
            String pw = null;
            try {
                pw = PasswordHelper.getEncryptedPassword(((TextView)findViewById(R.id.tfPasswort)).getText().toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String username = ((TextView)findViewById(R.id.tfName)).getText().toString();
            String url = "http://10.0.2.2:8080//loginUser?username=" + username + "&hashedPasswort=" + pw;

            HttpRequester.simpleStringRequest(this, url, (String response) -> login(response.equals("OK")), error -> login(false));
        });
        ((Button)findViewById(R.id.btNeuAccErstellen)).setOnClickListener(onClick -> {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });

        ((TextView)findViewById(R.id.textView3)).setOnClickListener(onClick -> {
            HttpRequester.simpleJsonArrayRequest(this, "http://10.0.2.2:8080/loadFahrtenListe?kennzeichen=B OB 385", jsonResponse -> {
                Log.d("onCreate: ", jsonResponse.toString());
                Toast.makeText(MainActivity.this, jsonResponse.toString(), Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(this, ListViewActivity.class);
                startActivity(myIntent);
            }, error -> Log.d("ERROR LISTENER:", error.toString()));
        });
    }
    public void login(boolean success){
        Toast toast = Toast.makeText(this, success ? "Gute Login" : "Schlechte Login",Toast.LENGTH_LONG);
        toast.show();
        if(){
            Intent myIntent = new Intent(this, MainActivity2.class);
            startActivity(myIntent);
        }
    }
}
