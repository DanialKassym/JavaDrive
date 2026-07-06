package com.example.JavaDrive.web.controller;

import com.example.JavaDrive.exception.FileNotFoundOnDiskException;
import com.example.JavaDrive.exception.ResourceNotFoundException;
import com.example.JavaDrive.exception.StorageException;
import com.example.JavaDrive.exception.StorageFileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(FileNotFoundOnDiskException.class)
    public ResponseEntity<String> handleFileNotFoundOnDisk(FileNotFoundOnDiskException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<String> handleStorageException(StorageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(StorageFileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

