plugins {
    kotlin("jvm") version "2.2.10"
    application
}

group = "net.minepact"
version = "0.1-DEV"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("net.minepact.mps.Main")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}