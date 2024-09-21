plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.azure.spring:azure-spring-boot-starter-keyvault-secrets:3.0.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("com.h2database:h2")
    //runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:11.2.0.jre8")
    // https://mvnrepository.com/artifact/io.kubernetes/client-java
    implementation("io.kubernetes:client-java:21.0.1")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Dependency Injection
    //implementation("io.insert-koin:koin-android:3.1.2")
    implementation("io.insert-koin:koin-core-jvm:3.2.2")

    testImplementation("org.mockito:mockito-core:4.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
