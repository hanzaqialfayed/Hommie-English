package com.example.hommieenglish.utils;

import android.util.Patterns;

import java.util.Base64;

public class Helper {
    public static String encodePassword(String password) {
        byte[] encodedBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encodedBytes = Base64.getEncoder().encode(password.getBytes());
        }
        return new String(encodedBytes);
    }

    public static String decodePassword(String encodedPassword) {
        byte[] decodedBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decodedBytes = Base64.getDecoder().decode(encodedPassword);
        }
        return new String(decodedBytes);
    }

    public static Boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches();
    }
}
