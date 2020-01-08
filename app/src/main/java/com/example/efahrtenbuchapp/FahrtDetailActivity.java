package com.example.efahrtenbuchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.eFahrtenbuch.User;
import com.example.efahrtenbuchapp.eFahrtenbuch.UserManager;
import com.example.efahrtenbuchapp.http.json.JSONConverter;
import com.example.efahrtenbuchapp.helper.PasswordHelper;
import com.example.efahrtenbuchapp.http.HttpRequester;
import com.example.efahrtenbuchapp.http.UrlBuilder;
import com.example.efahrtenbuchapp.utils.DateUtils;

import java.security.NoSuchAlgorithmException;

public class FahrtDetailActivity extends AppCompatActivity {

    public static final String ID = "FAHRT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fahrt_detail_activity);
        getSupportActionBar().hide();
        int value = getIntent().getIntExtra(ID, -1);
        TextView tv =  findViewById(R.id.etEndTimeDetail);
        tv.setText("XXXXXX - " + value);
        String url = new UrlBuilder().path("loadFahrt").param("fahrtid", Integer.toString(value)).build();
        HttpRequester.simpleJsonRequest(this, url, jsonResponse -> setFahrt(JSONConverter.createFahrtFromJSON(jsonResponse)),
                error -> Toast.makeText(this, "Fehler beim laden der Fahrt ID=" + value, Toast.LENGTH_SHORT));
    }

    /**
     * Setzt die Fahrt in die GUI ein
     * @param fahrt die einzusetzende Fahrt
     */
    public void setFahrt(Fahrt fahrt){
        Log.i("TRYING TO SET THE FAHRT:", fahrt.toString());
        //Start Adresse - Textfelder
        TextView StartTimeText = findViewById(R.id.etStartTimeDetail);
        TextView StartDateText = findViewById(R.id.etStartDateDetail);
        TextView StartStreetText = findViewById(R.id.etStartStreetDetail);
        TextView StartNumberText = findViewById(R.id.etStartNrDetail);
        TextView StartPLZText = findViewById(R.id.etStartPLZDetail);
        TextView StartOrtText = findViewById(R.id.etStartOrtDetail);

        StartTimeText.setText(DateUtils.getTime(fahrt.getFahrtBeginnZeit()));
        StartDateText.setText(DateUtils.getDate(fahrt.getFahrtBeginnDatum()));
        StartStreetText.setText(fahrt.getStartAdresse().getStrasse());
        StartNumberText.setText(fahrt.getStartAdresse().getHausnummer());
        StartPLZText.setText(Integer.toString(fahrt.getStartAdresse().getPlz()));
        StartOrtText.setText(fahrt.getStartAdresse().getOrt());

        //Ziel Adresse - Textfelder
        TextView EndTimeText = findViewById(R.id.etEndTimeDetail);
        TextView EndDateText = findViewById(R.id.etEndDateDetail);
        TextView EndStreetText = findViewById(R.id.etEndStreetDetail);
        TextView EndNumberText = findViewById(R.id.etEndNrDetail);
        TextView EndPLZText = findViewById(R.id.etEndPLZDetail);
        TextView EndOrtText = findViewById(R.id.etEndOrtDetail);

        EndTimeText.setText(DateUtils.getTime(fahrt.getFahrtEndeZeit()));
        EndDateText.setText(DateUtils.getDate(fahrt.getFahrtEndeDatum()));
        EndStreetText.setText(fahrt.getZielAdresse().getStrasse());
        EndNumberText.setText(fahrt.getZielAdresse().getHausnummer());
        EndPLZText.setText(Integer.toString(fahrt.getZielAdresse().getPlz()));
        EndOrtText.setText(fahrt.getZielAdresse().getOrt());

        //Andere Felder
        TextView reiseRoute = findViewById(R.id.etRouteDetail);
        TextView reiseZweck = findViewById(R.id.etZweckDetail);
        TextView besuchtePersonenFirmenBehoerden = findViewById(R.id.etZielDetail);
        TextView kmFahrtBeginn = findViewById(R.id.etStartKmDetail);
        TextView kmFahrtEnde = findViewById(R.id.etEndKmDetail);
        TextView kmGeschaeftlich = findViewById(R.id.etGeschaeftlichGefDetail);
        TextView kmPrivat = findViewById(R.id.etPrivatGefDetail);
        TextView kmWohnArbeit = findViewById(R.id.etArbeitswegDetail);
        TextView kraftstoffLiter = findViewById(R.id.etLiterbetragDetail);
        TextView kraftstoffBetrag = findViewById(R.id.etSprittkostenDetail);
        TextView literPro100km = findViewById(R.id.etVerbrauchDetail);
        TextView sonstigesBetrag = findViewById(R.id.etExtrasKostenDetail);

        reiseRoute.setText(fahrt.getReiseroute());
        reiseZweck.setText(fahrt.getReisezweck());
        besuchtePersonenFirmenBehoerden.setText(fahrt.getBesuchtePersonenFirmenBehoerden());
        kmFahrtBeginn.setText(Double.toString(fahrt.getKmFahrtBeginn()));
        kmFahrtEnde.setText(Double.toString(fahrt.getKmFahrtEnde()));
        kmGeschaeftlich.setText(Double.toString(fahrt.getKmGeschaeftlich()));
        kmPrivat.setText(Double.toString(fahrt.getKmPrivat()));
        kmWohnArbeit.setText(Double.toString(fahrt.getKmWohnArbeit()));
        kraftstoffLiter.setText(Double.toString(fahrt.getKraftstoffLiter()));
        kraftstoffBetrag.setText(Double.toString(fahrt.getKraftstoffBetrag()));
        literPro100km.setText(Double.toString(fahrt.getLiterPro100km()));
        sonstigesBetrag.setText(Double.toString(fahrt.getSonstigesBetrag()));
    }
}
