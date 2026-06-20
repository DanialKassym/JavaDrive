package com.example.JavaDrive.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailAddress {
    @NotNull(message = "The field cannot be null")
    @Email(message = "Invalid email format")
    private String email;

}
