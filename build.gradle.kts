import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    val kotlinVersion = "1.6.10"
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    //kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
}

group = "com.jaehl"
version = "1.0-SNAPSHOT"

val daggerVersion by extra("2.39.1")
val compose_version = "1.2.1"

repositories {
    jcenter()
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kapt {
    generateStubs = true
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                //implementation ("androidx.compose.runtime:runtime:$compose_version")

                // Dagger : A fast dependency injector for Android and Java.
                implementation("com.google.dagger:dagger-compiler:$daggerVersion")
                configurations.get("kapt").dependencies.add(DefaultExternalModuleDependency("com.google.dagger", "dagger-compiler", "$daggerVersion"))

                implementation ("org.jsoup:jsoup:1.8.3")
                implementation ("com.google.code.gson:gson:2.8.9")
                implementation("com.arkivanov.decompose:decompose:0.6.0")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.6.0")
                //implementation ("com.google.accompanist:accompanist-flowlayout:0.26.2-beta")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GameTools"
            packageVersion = "1.0.0"
        }
    }
}
