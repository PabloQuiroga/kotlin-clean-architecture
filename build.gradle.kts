plugins {
    // Plugin esencial para compilación de Kotlin en la JVM
    kotlin("jvm") version "2.4.20"
    application
}

application {
    mainClass.set("com.pablo.clean.MainKt")
}

group = "com.pablo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Corrutinas: Para el manejo asíncrono y Flow
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Koin: Para la Inyección de Dependencias (según ADR-003)
    implementation("io.insert-koin:koin-core:3.5.0")

    // JUnit 5: Para la validación de calidad
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")


}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}