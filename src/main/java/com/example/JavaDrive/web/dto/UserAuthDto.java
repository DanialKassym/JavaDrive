package com.example.JavaDrive.web.dto;


import org.springframework.security.core.userdetails.UserDetails;

public record UserAuthDto(String userId,UserDetails userDetails) {}
