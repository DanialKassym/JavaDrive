package com.example.JavaDrive;

import com.example.JavaDrive.Upload.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication()
@EnableConfigurationProperties(StorageProperties.class)
public class JavaDriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaDriveApplication.class, args);
	}
}
