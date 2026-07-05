package com.example.JavaDrive.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileDto {
    private Long id;
    private String original;
    private String type;
}

