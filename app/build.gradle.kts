plugins {
    application
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

version = "1.0.2"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    // Slf4j
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
    implementation("org.slf4j:slf4j-simple:2.1.0-alpha1")

    // Use JUnit test framework.
    testImplementation(libs.junit)

    // Azure SDK
    implementation("com.microsoft.azure.sdk.iot:iot-device-client:2.5.0")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // OSHI
    implementation("com.github.oshi:oshi-core:6.6.1")

    // Pi4J
    implementation("com.pi4j:pi4j-plugin-linuxfs:2.6.1")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.shadowJar {
    manifest {
        archiveClassifier.set("")
    }
    destinationDirectory.set(layout.buildDirectory.dir("jar"))
    archiveFileName.set("cda-${project.version}.jar")
    from(sourceSets.main.get().output)
}

application {
    // Define the main class for the application.
    mainClass = "org.cda.Main"
}
