plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "net.csik"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "MainKt"
}

kotlin {
    jvmToolchain(17)
}