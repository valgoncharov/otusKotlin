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

tasks.named<Jar>("jar") {
    archiveBaseName.set("time-app") // или оставьте как есть — не обязательно
    manifest {
        attributes["Main-Class"] = "MainKt" // ← важно!
    }
    // Включаем зависимости (на всякий случай, хотя у вас их почти нет)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations.named("runtimeClasspath").get().forEach { dep ->
        from(zipTree(dep))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}