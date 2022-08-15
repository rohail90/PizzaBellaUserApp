package com.orderpizaonline.pizzabella.model;

import android.util.Patterns;

public class SignupInfo {
    private String id;
    private String phoneNumber;
    private String name;
    private String email;
    private String confirmPassword;
    private String password;



    public SignupInfo(String id, String phoneNumber, String name, String email, String password, String cinfirmPassword) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = cinfirmPassword;

    }
    public SignupInfo(String phoneNumber, String name, String email, String password, String cinfirmPassword) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = cinfirmPassword;

    }

    public SignupInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isPhoneValid() {
        return getPhoneNumber().length() == 11;
    }


    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }
    public boolean isConfirmPasswordLengthGreaterThan5() {
        return getConfirmPassword().length() > 5;
    }
    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }
    public boolean isPasswordMatches() {
        return getPassword().matches(getConfirmPassword());
    }



}
