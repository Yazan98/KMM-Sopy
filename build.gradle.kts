plugins {
    id("com.android.library").version("8.0.2").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
    id("maven-publish")
}

group = "com.yazantarifi"
version = "1.0.0"

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
