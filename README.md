# JavaDrive

A backend file storage service built with Spring Boot that provides authenticated file management for individual users. The application combines JWT-based authentication, email verification, and persistent file storage behind a REST API with a minimal static web interface.

![Java](https://img.shields.io/badge/Java-25-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.0-6DB33F)
![Gradle](https://img.shields.io/badge/Gradle-Kotlin_DSL-02303A)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.7-4169E1)
![Spring Security](https://img.shields.io/badge/Spring_Security-Enabled-6DB33F)
![JWT](https://img.shields.io/badge/JWT-0.13.0-black)
![OpenAPI](https://img.shields.io/badge/OpenAPI-SpringDoc-85EA2D)

---

# Key Features

- JWT authentication using secure HTTP cookies.
- Email verification workflow for user registration.
- Secure file upload and download endpoints.
- User-specific file dashboard.
- PostgreSQL persistence with Spring Data JPA.
- Spring Security integration for endpoint protection.
- Multipart upload support (up to 100 MB by default).
- OpenAPI / Swagger UI integration.
- Global exception handling.
- Static frontend pages for authentication and file management.
- Configurable storage properties and JWT settings.

---

# Repository Structure

```text
JavaDrive/
├── build.gradle.kts                  # Gradle Kotlin DSL build configuration
├── settings.gradle.kts               # Gradle project settings
├── gradlew                           # Gradle wrapper (Unix)
├── gradlew.bat                       # Gradle wrapper (Windows)
├── LICENSE
├── README.md
├── files/                            # Sample storage directory
│   └── helloworld.txt
├── gradle/
│   └── wrapper/
├── src/
│   ├── gradle.properties
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/JavaDrive/
│   │   │       ├── JavaDriveApplication.java     # Application entry point
│   │   │       ├── config/                       # Application configuration
│   │   │       ├── domain/
│   │   │       │   ├── entity/                   # JPA entities
│   │   │       │   ├── enums/
│   │   │       │   └── repository/               # Spring Data repositories
│   │   │       ├── exception/                    # Custom exceptions
│   │   │       ├── security/                     # JWT and Spring Security
│   │   │       ├── utils/                        # Utility classes
│   │   │       └── web/
│   │   │           ├── controller/               # REST controllers
│   │   │           ├── dto/                      # DTOs
│   │   │           └── service/                  # Business logic
│   │   └── resources/
│   │       ├── application.example.properties
│   │       └── static/                           # HTML/CSS/JS frontend
```

---

# Getting Started

## Prerequisites

The project is configured with the following runtime requirements:

| Dependency | Version |
|------------|---------|
| Java | **25** (Gradle Toolchain) |
| Spring Boot | 3.4.0 |
| Gradle | Wrapper Included |
| PostgreSQL | Required |
| SMTP Server | Required for email verification |

> [IMPORTANT]
> Java 25 is explicitly configured in `build.gradle.kts`. Ensure your JDK matches the configured toolchain.

---

# Installation

Clone the repository:

```bash
git clone https://github.com/DanialKassym/JavaDrive.git
cd JavaDrive
```

Create an application configuration:

```bash
cp src/main/resources/application.example.properties \
src/main/resources/application.properties
```

Update the following properties inside `application.properties`:

```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

spring.mail.host=
spring.mail.port=
spring.mail.username=
spring.mail.password=

jwt.secret=
jwt.issuer=
```

Build the project:

```bash
./gradlew build
```

Run the application:

```bash
./gradlew bootRun
```

Or execute the packaged JAR:

```bash
./gradlew bootJar

java -jar build/libs/JavaDrive-0.0.1-SNAPSHOT.jar
```

---

# Usage Examples

## Upload a File

```bash
curl \
  --cookie "JWT=<your-jwt-cookie>" \
  -F "file=@document.pdf" \
  http://localhost:8081/api/v1/files/upload
```

---

## List User Files

```bash
curl \
  --cookie "JWT=<your-jwt-cookie>" \
  http://localhost:8081/api/v1/files/dashboard
```

---

## Download a File

```bash
curl \
  --cookie "JWT=<your-jwt-cookie>" \
  http://localhost:8081/api/v1/files/1 \
  --output downloaded-file
```

---

## Swagger UI

After starting the application:

```
http://localhost:8081/swagger-ui.html
```

---

# Testing

Run the project's test suite:

```bash
./gradlew test
```

Build and execute all verification tasks:

```bash
./gradlew build
```

---

> [NOTE]
> The repository includes Spring Boot Test, Spring Security Test and JUnit Platform test dependencies. If additional tests are added under `src/test`, they can be executed using the Gradle commands above.