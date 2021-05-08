import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
    application
}

version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-client-core:1.5.0")
    implementation("io.ktor:ktor-client-okhttp:1.5.0")
    implementation("io.ktor:ktor-server-core:1.5.0")
    implementation("io.ktor:ktor-server-netty:1.5.0")
    implementation("net.dv8tion:JDA:4.2.0_247")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("com.beust:klaxon:5.0.1")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.litote.kmongo:kmongo:4.2.3")

    implementation("io.insert-koin:koin-core:3.0.1")

    implementation("org.reflections:reflections:0.9.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "Main"
    }
}

application {
    mainClassName = "Main"
}