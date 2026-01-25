package com.example.JavaDrive.utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class EmailAddress {
    @NotNull(message = "The field cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    public EmailAddress(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
