plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("org.jlleitschuh.gradle.ktlint")
}

apply {
    from("$rootDir/android-common.gradle")
}

dependencies {
    // Test
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Version.Kotlin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.Coroutines}")
    testImplementation("androidx.test:core:${Version.TestCore}")
    testImplementation("io.mockk:mockk:${Version.MockK}")

    // Dagger
    api("com.google.dagger:dagger:${Version.Dagger}")
    api("com.google.dagger:dagger-android:${Version.Dagger}")
    api("com.google.dagger:dagger-android-support:${Version.Dagger}")
    kapt("com.google.dagger:dagger-compiler:${Version.Dagger}")
    kapt("com.google.dagger:dagger-android-processor:${Version.Dagger}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${Version.Firebase}"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
