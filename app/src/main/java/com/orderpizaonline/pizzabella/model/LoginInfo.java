package com.orderpizaonline.pizzabella.model;

public class LoginInfo {
    private String id;
    private String phoneNumber;
    private String password;



    public LoginInfo(String id, String phoneNumber, String password) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;

    }
    public LoginInfo( String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;

    }

    public LoginInfo() {
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


    public boolean isPhoneValid() {
        return getPhoneNumber().length() == 10;
    }


    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }

}
