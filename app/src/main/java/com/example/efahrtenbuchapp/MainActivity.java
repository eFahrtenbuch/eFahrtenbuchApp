package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.efahrtenbuchapp.eFahrtenbuch.User;
import com.example.efahrtenbuchapp.eFahrtenbuch.UserManager;
import com.example.efahrtenbuchapp.http.json.JSONConverter;
import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.HttpRequester;
import com.example.efahrtenbuchapp.http.UrlBuilder;

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
            String urlWithBuilder = new UrlBuilder().path("loginUser").param(username, username).param("hashedPasswort", pw).build();
            String url = new UrlBuilder().path("loginUser").param("username", username).param("hashedPasswort", pw).build();
            Log.d("LOGIN", "URL =  " + url);
            HttpRequester.simpleStringRequest(this, url, (String response) -> {
                dialog.hide();
                HttpRequester.simpleJsonRequest(this, new UrlBuilder().path("getUserbyUserName").param("username", username).build(),
                        jsonResponse -> {
                            Log.d("MainActivity -> onLogin: ", "jsonUserResponse = " + jsonResponse);
                            login(response.equals("OK"), JSONConverter.createObjectFromJSON(User.class, jsonResponse));
                        },
                        error -> {
                    failedLogin(error);
                    dialog.hide();
                        });
                }, error -> {
                failedLogin(error);
                dialog.hide();
            });
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
    public void login(boolean success, User user){
        Toast.makeText(this, success ? "Login erfolgreich" : "Login fehlgeschlagen!",Toast.LENGTH_LONG).show();

        if(success && user != null){
            Intent myIntent = new Intent(this, MainActivity2.class);
            startActivity(myIntent);
            UserManager.getInstance().setUser(user);
            //KANN WEGLog.d("LOGIN SUCCESFULL WITH USER = ", user.toString());
            this.finish();
        }
    }
    public void failedLogin(VolleyError error){
        error.printStackTrace();
        login(false, null);
    }
}
