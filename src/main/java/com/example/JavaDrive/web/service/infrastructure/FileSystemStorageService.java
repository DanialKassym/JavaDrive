package com.example.JavaDrive.web.service.infrastructure;

import com.example.JavaDrive.config.StorageProperties;
import com.example.JavaDrive.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getWindowsLocation());
        init();
    }

    public void store(MultipartFile file, String safeInternalName, String id) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path userDirectory = this.rootLocation.resolve("user_" + id).normalize().toAbsolutePath();

            Files.createDirectories(userDirectory);

            Path destinationFile = userDirectory.resolve(Paths.get(safeInternalName)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(userDirectory)) {
                throw new StorageException("Cannot store file outside user directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.");
        }
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }
    }

    public String getRootLocation() {
        return rootLocation.toString();
    }
}
