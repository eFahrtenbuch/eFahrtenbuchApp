package com.example.efahrtenbuchapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.efahrtenbuchapp.eFahrtenbuch.json.WebServiceRessources;
import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.HttpRequester;

import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Button btn = (Button) findViewById(R.id.btAccountErstellen);
        Log.d("", "button: " + btn);
        btn.setOnClickListener((event) -> {
            ProgressDialog dialog = ProgressDialog.show(RegisterActivity.this, "",
                    "Verbinde...", true);
            TextView userName = (TextView) findViewById(R.id.tfNameRegister);
            TextView password1 = (TextView) findViewById(R.id.tfPasswortRegister);
            TextView password2 = (TextView) findViewById(R.id.tfPasswort2Register);
            String hashedPasswort = null;
            try {
                hashedPasswort = PasswordHelper.getEncryptedPassword(password1.getText().toString());
                Log.d("", "onCreate: RegisterActivity: PW before hash: " + password1.getText().toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String url = "http://10.0.2.2:8080/registerUser?username=" + userName.getText().toString() + "&hashedPasswort=" + hashedPasswort + "&name=x&vorname=y";
            Log.d("", "onCreate: " + url);
            HttpRequester.simpleStringRequest(this, url, (String response) -> {
                message(response.equals("OK") ? "Erfolgreich registriert!" : "Registrierung nicht möglich");
                dialog.dismiss();
            }, error -> {
                message(WebServiceRessources.ERROR_NO_CONN_LONG);
                dialog.dismiss();
            });
        });
    }

    public void message(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}