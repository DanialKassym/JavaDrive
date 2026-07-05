package com.example.JavaDrive.exception;

public class FileNotFoundOnDiskException extends RuntimeException {
    public FileNotFoundOnDiskException(String message) {
        super(message);
    }
}
