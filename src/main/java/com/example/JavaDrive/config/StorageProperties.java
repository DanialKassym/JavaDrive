package com.example.JavaDrive.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private final String windowsLocation = "C:\\IntellijProjects\\JavaDrive\\files";
}
