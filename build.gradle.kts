plugins {
	java
	id("org.springframework.boot") version "3.4.0"
}

group = "JavaDrive"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenLocal()
	mavenCentral()
}
dependencies {
	implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.0"))
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	implementation ("org.springframework.data:spring-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.postgresql:postgresql:42.7.7")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.14")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
