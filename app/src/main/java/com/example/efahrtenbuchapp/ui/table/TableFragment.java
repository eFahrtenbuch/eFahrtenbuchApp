package com.example.efahrtenbuchapp.ui.table;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.example.efahrtenbuchapp.R;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListenAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.json.JSONConverter;
import com.example.efahrtenbuchapp.http.HttpRequester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableFragment extends Fragment {

    private TableViewModel tableViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tableViewModel =
                ViewModelProviders.of(this).get(TableViewModel.class);
        View root = inflater.inflate(R.layout.fragment_table, container, false);
        /*final TextView textView = root.findViewById(R.id.text_gallery);
        tableViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.listview);
        ListView lv = root.findViewById(R.id.listviewid);
        HttpRequester.simpleJsonArrayRequest(getActivity(), "http://10.0.2.2:8080/loadFahrtenListe?kennzeichen=B OB 385", jsonResponse -> {
            Log.d("onCreate: ", jsonResponse.toString());
            List<FahrtListAdapter> list = JSONConverter.toJSONList(jsonResponse).stream()//
                    .map(json -> {
                        Log.d("SEND JSON IN REQUEST BODY: ", json.toString());
                        //HttpRequester.simpleJsonRequest(getActivity(), Request.Method.POST, "http://10.0.2.2:8080/insertFahrt", json, null, null);

                        return (Fahrt) JSONConverter.createFahrtFromJSON(json);
                    })//
                    .map(fahrt -> new FahrtListAdapter(fahrt))
                    .collect(Collectors.toList());
            refreshList(root, list);
        }, null);

        FahrtListenAdapter adapter = new FahrtListenAdapter(getActivity(), R.layout.listview, new ArrayList());
        lv.setAdapter(adapter);
        return root;
    }

    public void message(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void refreshList(View root, List<FahrtListAdapter> list){
        ListView lv = root.findViewById(R.id.listviewid);
        ArrayAdapter adapter = new FahrtListenAdapter(getActivity(), R.layout.fahrt_list_adapter, list);
        lv.setAdapter(adapter);
    }
}