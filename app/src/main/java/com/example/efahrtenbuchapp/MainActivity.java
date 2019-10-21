package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private Button ButtonNewAcc;

    AtomicReference<Boolean> notYetClicked = new AtomicReference<>(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButtonNewAcc = (Button) findViewById(R.id.btNeuAccErstellen);
        ButtonNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
            }
        });


    }
}
