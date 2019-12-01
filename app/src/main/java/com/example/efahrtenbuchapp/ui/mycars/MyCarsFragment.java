package com.example.efahrtenbuchapp.ui.mycars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.efahrtenbuchapp.R;

public class MyCarsFragment extends Fragment {

    private MyCarsViewModel myCarsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCarsViewModel =
                ViewModelProviders.of(this).get(MyCarsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mycars, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        myCarsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}