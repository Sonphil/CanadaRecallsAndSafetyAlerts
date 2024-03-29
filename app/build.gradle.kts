import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.Compose
    }
}

dependencies {
    implementation("com.google.android.material:material:${Version.MaterialComponents}")
    implementation("androidx.appcompat:appcompat:${Version.AppCompat}")
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
    implementation("androidx.hilt:hilt-navigation-fragment:${Version.HiltAndroid}")
    implementation("androidx.hilt:hilt-work:${Version.HiltAndroid}")
    kapt("androidx.hilt:hilt-compiler:${Version.HiltAndroid}")

    // Compose
    implementation("androidx.compose.ui:ui:${Version.Compose}")
    implementation("androidx.compose.ui:ui-tooling:${Version.Compose}")
    implementation("androidx.compose.material:material:${Version.Compose}")
    implementation("androidx.compose.material:material-icons-core:${Version.Compose}")
    implementation("androidx.compose.material:material-icons-extended:${Version.Compose}")
    implementation("androidx.compose.runtime:runtime-livedata:${Version.Compose}")
    implementation("androidx.compose.ui:ui-viewbinding:${Version.Compose}")

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

    // Hilt
    implementation("com.google.dagger:hilt-android:${Version.Hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Version.Hilt}")

    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Version.LeakCanary}")

    // Toasty
    implementation("com.github.GrenderG:Toasty:${Version.Tosty}")

    // RecyclerView Animators
    implementation("jp.wasabeef:recyclerview-animators:${Version.RecyclerViewAnimators}")

    implementation(project(":domain"))
    implementation(project(":data"))
}
