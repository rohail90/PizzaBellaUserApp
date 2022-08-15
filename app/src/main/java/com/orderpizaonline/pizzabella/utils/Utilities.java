package com.orderpizaonline.pizzabella.utils;

public class Utilities {
    public static boolean emailValidator(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
