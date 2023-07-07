import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import com.android.build.gradle.tasks.GenerateBuildConfig

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("signing")
    id("org.gradle.maven-publish")
    id("org.jetbrains.dokka") version "1.4.20"
}

group = "com.yazantarifi"
version = "1.0.1"

kotlin {

    // Android Configurations
    android {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    tasks.withType<GenerateBuildConfig> {
        isEnabled = false
    }

    // IOS Configurations
    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm32(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "sopy"
            isStatic = true
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("io.ktor:ktor-client-core:2.3.0")
                implementation("io.ktor:ktor-client-logging:2.3.0")
                implementation("io.ktor:ktor-client-serialization:2.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("com.google.dagger:hilt-android:2.44")
                implementation("io.ktor:ktor-client-android:2.3.0")
                implementation("io.ktor:ktor-client-json:2.3.0")
                implementation("io.ktor:ktor-client-serialization-jvm:2.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosArm32Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosArm32Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("io.ktor:ktor-client-ios:2.3.0")
                implementation("io.ktor:ktor-client-darwin:2.3.0")
                implementation("io.ktor:ktor-client-darwin-legacy:2.3.0")
            }
        }
    }
}

android {
    namespace = "com.yazantarifi.kmm.sopy"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

publishing {
    repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "OSSRH"

        credentials {
            username = ""
            password = ""
        }
    }

    publications.withType<MavenPublication> {
        artifact(javadocJar)
        pom {
            name.set("Sopy")
            description.set("Utility Classes, Android, IOS")
            url.set("https://github.com/Yazan98/Sopy")
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://github.com/Yazan98/Sopy/blob/main/LICENSE")
                    distribution.set("repo")
                }
            }

            developers {
                developer {
                    id.set("Yazan98")
                    name.set("Yazan Tarifi")
                }
            }

            scm {
                connection.set("scm:git:ssh://github.com/Yazan98/Sopy.git")
                developerConnection.set("scm:git:ssh://github.com/Yazan98/Sopy.git")
                url.set("https://github.com/Yazan98/Sopy")
            }
        }
        the<SigningExtension>().sign(this)
    }
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(tasks.withType<Sign>())
}