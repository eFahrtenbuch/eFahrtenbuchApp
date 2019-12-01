package com.example.efahrtenbuchapp.ui.mycars;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyCarsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyCarsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}