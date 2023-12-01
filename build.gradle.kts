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
    testImplementation(kotlin("test"))
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