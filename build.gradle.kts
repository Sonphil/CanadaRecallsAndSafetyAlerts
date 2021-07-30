// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Version.Gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.Kotlin}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Version.KtlintGradle}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Version.Navigation}")
        classpath("com.google.gms:google-services:${Version.GoogleServices}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Version.FirebaseGradle}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Version.Hilt}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Version.GradleVersions}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

apply(plugin = "com.github.ben-manes.versions")

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}