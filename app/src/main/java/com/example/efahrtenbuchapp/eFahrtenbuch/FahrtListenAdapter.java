package com.example.efahrtenbuchapp.eFahrtenbuch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.efahrtenbuchapp.R;

import java.util.List;

public class FahrtListenAdapter extends ArrayAdapter<FahrtListAdapter> {
    private final int resource;
    private Context context;

    public FahrtListenAdapter(@NonNull Context context, int resource, List<FahrtListAdapter> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String km = getItem(position).getKm();
        String ziel = getItem(position).getZiel();
        String datum = getItem(position).getDatum();
        FahrtListAdapter fla = new FahrtListAdapter(datum, ziel, km);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.fahrt_list_adapter, parent, false);

        TextView tv1 = convertView.findViewById(R.id.tv1);
        TextView tv2 = convertView.findViewById(R.id.tv2);
        TextView tv3 = convertView.findViewById(R.id.tv3);
        tv1.setText(ziel);
        tv2.setText(datum);
        tv3.setText(km);
        return convertView;
    }
}
