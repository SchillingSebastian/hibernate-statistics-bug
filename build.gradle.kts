import org.jetbrains.kotlin.noarg.gradle.NoArgExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"

    id("org.jetbrains.kotlin.plugin.noarg") version "1.8.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.21"

    application
}

group = "org.hibernate.bugs"
version = "1.0-SNAPSHOT"

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.Enumerated")
}

noArg {
    annotation("jakarta.persistence.Entity")
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hibernate.orm:hibernate-core:6.4.2.Final")
    implementation("org.hibernate.orm:hibernate-testing:6.4.2.Final")
    testRuntimeOnly("com.h2database:h2:2.2.224")
    testImplementation(kotlin("test"))
}
configure<NoArgExtension> {
    annotation("jakarta.persistence.Entity")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}