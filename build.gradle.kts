plugins {
	kotlin("jvm") version "2.2.20"
	kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "2.2.20"
    kotlin("plugin.noarg") version "2.2.20"
    id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.np"
version = "0.0.1"
description = "bugs"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation(project(":np-auth-common"))
    implementation("jakarta.validation:jakarta.validation-api")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-mysql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
