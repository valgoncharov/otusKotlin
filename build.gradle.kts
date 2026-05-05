plugins {
    kotlin("jvm") version "2.0.21"
    application
}

sourceSets {
    main {
        kotlin {
            exclude("homework_3_new.kt")
            exclude("homeworkDSL/dsl_dz.kt")
            exclude("homeworkDSL/homework_3_pro.kt")
        }
    }
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

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("io.qameta.allure:allure-junit5:2.29.0")
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

tasks.test {
    useJUnitPlatform()
    systemProperty("allure.results.directory", "$projectDir/reports/allure-results")
}