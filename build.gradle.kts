import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    application
}

version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("net.dv8tion:JDA:4.2.1_262") {
        exclude(
            module = "opus-java"
        )
    }

    val logbackVersion = "1.2.3"
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")

    implementation("org.apache.commons:commons-math3:3.6.1")

    implementation("org.litote.kmongo:kmongo:4.2.3")

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    implementation("io.insert-koin:koin-core:3.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC")

    implementation("org.reflections:reflections:0.9.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "15"
}

tasks.withType<Jar> {
    archiveFileName.set("bot.jar")

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }

    manifest {
        attributes["Main-Class"] = "Main"
    }
}

application {
    mainClassName = "Main"
}