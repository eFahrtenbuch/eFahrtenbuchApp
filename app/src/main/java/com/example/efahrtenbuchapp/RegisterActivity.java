package com.example.efahrtenbuchapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.efahrtenbuchapp.eFahrtenbuch.json.WebServiceRessources;
import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.SimpleRequest;

public class RegisterActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Button btn = (Button) findViewById(R.id.btAccountErstellen);
        Log.d("", "button: " + btn);
        btn.setOnClickListener((event) -> {

            TextView userName = (TextView) findViewById(R.id.tfNameRegister);
            TextView password1 = (TextView) findViewById(R.id.tfPasswortRegister);
            TextView password2 = (TextView) findViewById(R.id.tfPasswort2Register);
            String hashedPasswort = PasswordHelper.getEncryptedPassword(password1.getText().toString());
            String url = "http://10.0.2.2:8080/registerUser?username=" + userName.getText().toString() + "&hashedPasswort=" + hashedPasswort + "&name=x&vorname=y";
            Log.d("", "onCreate: " + url);
            SimpleRequest.simpleResponse(this, url, (String response) -> {
                if(response.equals("OK")){
                    message("GUT");
                }
                else{
                    message("SCHLECHT");
                }
            }, error -> message(WebServiceRessources.ERROR_MSG_NO_CONN));
        });

    }

    public void message(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}