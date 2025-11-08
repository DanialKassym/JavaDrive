package com.example.JavaDrive.OpenApi;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0", description = "My API Documentation"))
public class OpenApiConfig {
    // No additional beans are typically required for basic setup
}
