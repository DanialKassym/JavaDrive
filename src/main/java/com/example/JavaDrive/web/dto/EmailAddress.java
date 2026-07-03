package com.example.JavaDrive.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailAddress {
    @NotNull(message = "The field cannot be null")
    @Email(message = "Invalid email format")
    @Size(max = 254, message = "Email Address cant be longer than 254 characters")
    private String email;
}
