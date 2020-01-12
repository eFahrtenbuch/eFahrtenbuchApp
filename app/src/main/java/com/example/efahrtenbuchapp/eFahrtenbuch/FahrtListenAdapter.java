package com.example.efahrtenbuchapp.eFahrtenbuch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.efahrtenbuchapp.FahrtDetailActivity;
import com.example.efahrtenbuchapp.R;

import java.util.List;

public class FahrtListenAdapter extends ArrayAdapter<FahrtListAdapter> {
    private final int resource;
    private Context context;

    /**
     * Konstruktor
     * @param context
     * @param resource
     * @param objects
     */

    public FahrtListenAdapter(@NonNull Context context, int resource, List<FahrtListAdapter> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    /**
     * Erstellt einzelne Elemente die in der Fahrtenliste angezeigt werden.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.fahrt_list_adapter, parent, false);

        ((TextView)convertView.findViewById(R.id.tv1)).setText(getItem(position).getZiel());
        ((TextView)convertView.findViewById(R.id.tv2)).setText(getItem(position).getDatum());
        ((TextView)convertView.findViewById(R.id.tv3)).setText(getItem(position).getKm());
        convertView.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), FahrtDetailActivity.class);
            i.putExtra(FahrtDetailActivity.ID, getItem(position).getId());
            getContext().startActivity(i);
            Log.i("ADAPTER", "onClick: ELEMENT= " + position);
        });
        return convertView;
    }

}
