package com.example.JavaDrive.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class FileDownloadDetails {
    private Resource resource;
    private String original_name;
}
