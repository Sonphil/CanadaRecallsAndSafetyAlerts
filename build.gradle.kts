// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Version.Gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.Kotlin}")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:${Version.KtlintGradle}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Version.Navigation}")
        classpath("com.google.gms:google-services:${Version.GoogleServices}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Version.FirebaseGradle}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}