package com.example.efahrtenbuchapp.eFahrtenbuch;

import com.example.efahrtenbuchapp.utils.DateUtils;

public class FahrtListAdapter {
    private String datum;
    private String ziel;
    private String km;

    public FahrtListAdapter(Fahrt fahrt){
        this.datum = DateUtils.getDate(fahrt.getFahrtBeginnDatum()) + "/" + DateUtils.getTime(fahrt.getFahrtBeginnZeit());
        this.ziel = fahrt.getZielAdresse().getOrt();
        this.km = Double.toString(fahrt.getKmFahrtEnde() - fahrt.getKmFahrtBeginn());
    }

    public FahrtListAdapter(String datum, String ziel, String km) {
        this.datum = datum;
        this.ziel = ziel;
        this.km = km;
    }

    public String getDatum() {
        return datum;
    }

    public String getZiel() {
        return ziel;
    }

    public String getKm() {
        return km;
    }
}
