package com.orderpizaonline.pizzabella.viewmodels;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orderpizaonline.pizzabella.model.LoginInfo;


public class LoginViewModel  extends ViewModel {

    public MutableLiveData<String> PhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<LoginInfo> userMutableLiveData;

    public MutableLiveData<LoginInfo> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {

        LoginInfo loginUser = new LoginInfo(PhoneNumber.getValue(), Password.getValue());

        userMutableLiveData.setValue(loginUser);

    }
}
