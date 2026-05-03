plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "com.otus.kotlinqa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // Для работы с HTTP запросами
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Для работы с JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // Для работы с MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.0")
    implementation("org.mongodb:mongodb-driver-sync:4.11.0")

    // Для логирования
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = listOf(
        "-Xms256m",
        "-Xmx1g",
        "-XX:MaxMetaspaceSize=256m",
        "-XX:+UseG1GC",
        "-XX:+HeapDumpOnOutOfMemoryError",
        "-Dfile.encoding=UTF-8"
    )
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "-Xms256m",      // Initial heap size
        "-Xmx1g",      // Maximum heap size
        "-XX:MaxMetaspaceSize=256m",
        "-XX:+UseG1GC", // Use G1 garbage collector
        "-XX:+HeapDumpOnOutOfMemoryError",
        "-XX:HeapDumpPath=./heap-dump.hprof"
    )
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}