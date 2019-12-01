package com.example.efahrtenbuchapp.ui.createNew;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateNewViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateNewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}