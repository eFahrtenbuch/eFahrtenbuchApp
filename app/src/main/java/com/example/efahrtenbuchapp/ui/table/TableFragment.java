package com.example.efahrtenbuchapp.ui.table;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.efahrtenbuchapp.R;
import com.example.efahrtenbuchapp.eFahrtenbuch.Fahrt;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.FahrtListenAdapter;
import com.example.efahrtenbuchapp.eFahrtenbuch.UserManager;
import com.example.efahrtenbuchapp.http.json.JSONConverter;
import com.example.efahrtenbuchapp.http.HttpRequester;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableFragment extends Fragment {

    private TableViewModel tableViewModel;
    private List<FahrtListAdapter> list;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        tableViewModel = ViewModelProviders.of(this).get(TableViewModel.class);

        View root = inflater.inflate(R.layout.fragment_table, container, false);

        ListView lv = root.findViewById(R.id.listviewidfrag);



        HttpRequester.simpleJsonArrayRequest(getActivity(), "http://10.0.2.2:8080/loadFahrtenListeForUser?userid="+ UserManager.getInstance().getUser().getId(), jsonResponse -> {
            Log.d("onCreate: ", jsonResponse.toString());
            List<FahrtListAdapter> list = JSONConverter.toJSONList(jsonResponse).stream()
                    .map(json -> (Fahrt) JSONConverter.createFahrtFromJSON(json))
                    .peek(fahrt -> Log.d("TABLEFRAGMENT", fahrt.toString()))
                    .map(fahrt -> new FahrtListAdapter(fahrt))
                    .collect(Collectors.toList());
            refreshList(lv, list);
        }, null);

        FahrtListenAdapter adapter = new FahrtListenAdapter(getActivity(), R.layout.fahrt_list_adapter, new ArrayList());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "DDD", Toast.LENGTH_LONG);
                System.out.println("dddddd");
                Log.d("KLICKED ON: ", "Item: " + list.get(position).toString());
            }
        });
        return root;
    }

    public void message(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void refreshList(View root, List<FahrtListAdapter> list){
        this.list = list;
        ListView lv = getActivity().findViewById(R.id.listviewidfrag);          //R.layout.fahrt_list_adapter
        ArrayAdapter adapter = new FahrtListenAdapter(getActivity(), R.layout.fahrt_list_adapter, list);
        lv.setAdapter(adapter);
    }
}