package com.example.MainApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*@RestController
@SpringBootApplication(scanBasePackages = {
		"com/example/AuthService/controllers", "com/example/AuthService/models", "com/example/AuthService/repository"
})
@EnableJpaRepositories(basePackages = "com/example/AuthService/repository")
@EntityScan(basePackages = "com/example/AuthService/models")*/
@SpringBootApplication(scanBasePackages = {
		"com/example/Email","com/example/MainApplication/controllers"
})
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}
