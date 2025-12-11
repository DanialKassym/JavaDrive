package com.example.JavaDrive.utils;

public class JwtRequest {
    public String username;
    public String password;
    public String email;

    public JwtRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
