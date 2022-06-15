val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")
    implementation("com.squareup.okhttp3:okhttp:4.8.1")

    //implementation ("com.intelligt.modbus:jlibmodbus:1.2.9.5")
    implementation ("com.intelligt.modbus:jlibmodbus:1.2.9.1")
    //implementation("com.github.kochedykov:jlibmodbus:1.2.9.1")

    //implementation("com.ghgande:j2mod:2.7.0")
   // implementation("com.github.infiniteautomation:modbus4j:v2.0.7")

    implementation("org.rxtx:rxtx:2.1.7")


    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
   // testImplementation("org.mockito:mockito-core:3.+")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

}