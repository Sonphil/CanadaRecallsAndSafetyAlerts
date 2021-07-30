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

    // Hilt
    api("com.google.dagger:hilt-android:${Version.Hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Version.Hilt}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${Version.Firebase}"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
