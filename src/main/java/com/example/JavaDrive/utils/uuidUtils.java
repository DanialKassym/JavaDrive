package com.example.JavaDrive.utils;

import java.util.UUID;

public class uuidUtils {
    public static String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }
}
