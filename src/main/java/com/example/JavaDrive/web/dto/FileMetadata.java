package com.example.JavaDrive.web.dto;

public record FileMetadata(
        String originalName,
        String safeInternalName,
        String extension
) {}

