package uk.ac.shef.oak.com4510.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Show current date\nand time automatically\ncalculated");
    }

    public LiveData<String> getText() {
        return mText;
    }
}