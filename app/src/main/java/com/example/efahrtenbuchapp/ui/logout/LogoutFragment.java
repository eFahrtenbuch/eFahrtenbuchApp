package com.example.efahrtenbuchapp.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.efahrtenbuchapp.R;
import com.example.efahrtenbuchapp.eFahrtenbuch.UserManager;

public class LogoutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogoutViewModel logoutViewModel = ViewModelProviders.of(this).get(LogoutViewModel.class);
        logoutViewModel.getText().observe(this, s -> {
            UserManager.getInstance().setUser(null);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }
}