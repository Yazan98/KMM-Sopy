import java.net.URI

plugins {
    id("com.android.library").version("8.0.2").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
    id("maven-publish")
}

group = "com.yazantarifi"
version = "1.0.1"

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven { url = URI("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    }
}
