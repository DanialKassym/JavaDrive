package com.example.JavaDrive.utils;

import java.util.regex.Pattern;

public class EmailValidate {
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
