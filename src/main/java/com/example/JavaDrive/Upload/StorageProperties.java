package com.example.JavaDrive.Upload;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String windowsLocation = "C:\\IntellijProjects\\JavaDrive\\files";

    private String linuxLocation = "/tmp/files";

    public String getLinuxLocation() {
        return linuxLocation;
    }

    public void setLinuxLocation(String linuxLocation) {
        this.linuxLocation = linuxLocation;
    }

    public String getWindowsLocation() {
        return windowsLocation;
    }

    public void setWindowsLocation(String windowsLocation) {
        this.windowsLocation = windowsLocation;
    }

}
