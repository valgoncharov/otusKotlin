plugins {
    kotlin("jvm") version "2.0.21"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.otus.kotlinqa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("homework.MainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}


//tasks.jar {
//    manifest {
//        attributes["Main-Class"] = "MainKt"
//    }
//    // Опционально: для копирования зависимостей в jar
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}