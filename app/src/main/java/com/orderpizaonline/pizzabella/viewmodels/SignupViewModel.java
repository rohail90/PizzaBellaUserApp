package com.orderpizaonline.pizzabella.viewmodels;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orderpizaonline.pizzabella.model.SignupInfo;


public class SignupViewModel extends ViewModel {

    public MutableLiveData<String> PhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> UserName = new MutableLiveData<>();
    public MutableLiveData<String> UserEmail = new MutableLiveData<>();
    public MutableLiveData<String> ConfirmPassword = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<SignupInfo> userMutableLiveData;

    public MutableLiveData<SignupInfo> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {

        SignupInfo loginUser = new SignupInfo(PhoneNumber.getValue(), UserName.getValue(), UserEmail.getValue(), Password.getValue(), ConfirmPassword.getValue());

        userMutableLiveData.setValue(loginUser);

    }
}
