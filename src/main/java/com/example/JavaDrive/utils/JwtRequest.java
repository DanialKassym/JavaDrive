package com.example.JavaDrive.utils;

public class JwtRequest {
    public String username;
    public String password;

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
