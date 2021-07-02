import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

apply {
    from("$rootDir/android-common.gradle")
}

android {
    defaultConfig {
        val versionMajor = 1
        val versionMinor = 0
        val versionPatch = 0
        val versionBuild = System.getenv("GITHUB_RUN_NUMBER")?.toInt() ?: 0

        applicationId = "com.sonphil.canadarecallsandsafetyalerts"
        versionCode = versionMajor * 10000 + versionMinor * 1000 + versionPatch * 100 + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch.$versionBuild"
        println("versionName: $versionName")
    }

    signingConfigs {
        val keyPropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(keyPropertiesFile.reader())

        val keyStoreFilePath = "./${keystoreProperties.getProperty("releaseKeyStore")}"

        create("release") {
            keyAlias = keystoreProperties.getProperty("releaseKeyAlias")
            keyPassword = keystoreProperties.getProperty("releaseKeyPassword")
            storeFile = rootProject.file(keyStoreFilePath)
            storePassword = keystoreProperties.getProperty("releaseStorePassword")
        }
    }

    buildTypes {
        val enableCrashReportingKey = "enableCrashReporting"

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            manifestPlaceholders[enableCrashReportingKey] = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders[enableCrashReportingKey] = true
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.android.material:material:${Version.MaterialComponents}")
    implementation("androidx.browser:browser:${Version.Browser}")
    implementation("androidx.constraintlayout:constraintlayout:${Version.ConstraintLayout}")
    implementation("androidx.core:core-ktx:${Version.CoreKtx}")
    implementation("androidx.fragment:fragment-ktx:${Version.Fragment}")
    implementation("androidx.preference:preference-ktx:${Version.Preference}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Version.LifecycleExtensions}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Version.SwipeRefreshLayout}")
    kapt("androidx.lifecycle:lifecycle-compiler:${Version.Lifecycle}")
    implementation("androidx.lifecycle:lifecycle-common-java8:${Version.Lifecycle}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Version.Navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${Version.Navigation}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LifecycleViewModelKtx}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Version.Lifecycle}")

    // Test
    testImplementation("junit:junit:${Version.JUnit}")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:${Version.WorkManager}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${Version.Firebase}"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Glide
    implementation("com.github.bumptech.glide:glide:${Version.Glide}")
    kapt("com.github.bumptech.glide:compiler:${Version.Glide}")

    // UltraViewPager
    implementation("com.alibaba.android:ultraviewpager:${Version.UltraViewPager}") {
        isTransitive = true
    }

    // Dagger
    implementation("com.google.dagger:dagger:${Version.Dagger}")
    implementation("com.google.dagger:dagger-android:${Version.Dagger}")
    implementation("com.google.dagger:dagger-android-support:${Version.Dagger}")
    kapt("com.google.dagger:dagger-compiler:${Version.Dagger}")
    kapt("com.google.dagger:dagger-android-processor:${Version.Dagger}")

    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Version.LeakCanary}")

    // Toasty
    implementation("com.github.GrenderG:Toasty:${Version.Tosty}")

    // RecyclerView Animators
    implementation("jp.wasabeef:recyclerview-animators:${Version.RecyclerViewAnimators}")

    implementation(project(":domain"))
    implementation(project(":data"))
}
