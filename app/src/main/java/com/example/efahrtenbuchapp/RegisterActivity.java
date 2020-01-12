package com.example.efahrtenbuchapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.efahrtenbuchapp.http.UrlBuilder;
import com.example.efahrtenbuchapp.http.WebServiceRessources;
import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.HttpRequester;

import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        findViewById(R.id.btAccountErstellen).setOnClickListener(click -> doRegister());
        ((TextView)findViewById(R.id.tfPasswort2Register)).setOnEditorActionListener((v, actionId, event) -> doRegister());
    }

    /**Führt den Registrierungsprozess durch*/
    private boolean doRegister() {
        ProgressDialog dialog = ProgressDialog.show(RegisterActivity.this, "",
                "Verbinde...", true);
        TextView userName = (TextView) findViewById(R.id.tfNameRegister);
        TextView password1 = (TextView) findViewById(R.id.tfPasswortRegister);
        TextView password2 = (TextView) findViewById(R.id.tfPasswort2Register);
        String hashedPasswort = null;
        try {
            hashedPasswort = PasswordHelper.getEncryptedPassword(password1.getText().toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String urlWithBuilder = new UrlBuilder().path("registerUser")
                .param("username", userName.getText().toString())
                .param("hashedPasswort", hashedPasswort)
                .param("name", " ")
                .param("vorname", " ").build();
        String url = "http://10.0.2.2:8080/registerUser?username=" + userName.getText().toString() + "&hashedPasswort=" + hashedPasswort + "&name=x&vorname=y";

        HttpRequester.simpleStringRequest(this, urlWithBuilder, (String response) -> {
            boolean success = response.equals("OK");
            message(success ? "Erfolgreich registriert!" : "Registrierung nicht möglich");
            dialog.dismiss();
            if(success){
                //Beendet die RegisterActivity wenn Registration erfolgreich
                //damit sich der user direkt einloggen kann.
                this.finish();
            }
        }, error -> {
            message(WebServiceRessources.ERROR_NO_CONN_LONG);
            dialog.dismiss();
        });
        return false;
    }

    public void message(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}