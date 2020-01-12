package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        //Listener für den Login-Button
        Button loginButton = findViewById(R.id.btAnmelden);
        loginButton.setOnClickListener(this::doLoginCheck);
        TextView passwordFeld = findViewById(R.id.tfPasswort);
        passwordFeld.setOnEditorActionListener((v, actionId, event) -> doLoginCheck(v));

        ((Button)findViewById(R.id.btNeuAccErstellen)).setOnClickListener(onClick -> {
            //Öffnet das Registrierungsfenster
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });
    }

    private boolean doLoginCheck(View v) {
        ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",
                "Verbinde...", true);
        String pw = null;
        try {
            //Verschlüsselt das Passwort bevor es abgeschickt wird
            pw = PasswordHelper.getEncryptedPassword(((TextView)findViewById(R.id.tfPasswort)).getText().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String username = ((TextView)findViewById(R.id.tfName)).getText().toString();
        String url = new UrlBuilder().path("loginUser").param("username", username).param("hashedPasswort", pw).build();
        HttpRequester.simpleStringRequest(this, url, (String response) -> {
            dialog.hide();
            HttpRequester.simpleJsonRequest(this, new UrlBuilder().path("getUserbyUserName").param("username", username).build(),
                    jsonResponse -> {
                        dialog.hide();
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
        return true;
    }

    /**
     * Prüft den Login und leitet ggf. Weiter
     * @param success
     * @param user
     */
    public void login(boolean success, User user){
        Toast.makeText(this, success ? "Login erfolgreich" : "Login fehlgeschlagen!",Toast.LENGTH_LONG).show();

        if(success && user != null){
            Intent myIntent = new Intent(this, NavigationActivity.class);
            startActivity(myIntent);
            UserManager.getInstance().setUser(user);
            this.finish();
        }
    }

    /**
     * Fehler beim Login
     * @param error
     */
    public void failedLogin(VolleyError error){
        error.printStackTrace();
        login(false, null);
    }
}
