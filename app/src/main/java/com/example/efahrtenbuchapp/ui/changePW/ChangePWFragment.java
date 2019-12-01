package com.example.efahrtenbuchapp.ui.changePW;

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

public class ChangePWFragment extends Fragment {

    private ChangePWViewModel changePWViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changePWViewModel =
                ViewModelProviders.of(this).get(ChangePWViewModel.class);
        View root = inflater.inflate(R.layout.fragment_changepw, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        changePWViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}