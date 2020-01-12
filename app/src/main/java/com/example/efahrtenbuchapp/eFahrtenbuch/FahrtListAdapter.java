package com.example.efahrtenbuchapp.eFahrtenbuch;

import com.example.efahrtenbuchapp.utils.DateUtils;

public class FahrtListAdapter {
    private int id;
    private String datum;
    private String ziel;
    private String km;

    /**
     * Konstruktor
     * @param fahrt
     */
    public FahrtListAdapter(Fahrt fahrt){
        this.id = fahrt.getId();
        this.datum = DateUtils.getDate(fahrt.getFahrtBeginnDatum()) + "/" + DateUtils.getTime(fahrt.getFahrtBeginnZeit());
        this.ziel = fahrt.getReisezweck();
        this.km = Double.toString(fahrt.getKmFahrtEnde() - fahrt.getKmFahrtBeginn());
    }


    //GETTER & SETTER
    public String getDatum() {
        return datum;
    }

    public String getZiel() {
        return ziel;
    }

    public String getKm() {
        return km;
    }

    public int getId(){ return id; }

    @Override
    public String toString() {
        return "FahrtListAdapter{" + "datum='" + datum + '\'' + ", ziel='" + ziel + '\'' + ", km='" + km + '\'' +'}';
    }
}
