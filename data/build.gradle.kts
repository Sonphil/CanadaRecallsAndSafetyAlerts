plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jlleitschuh.gradle.ktlint")
}

apply {
    from("$rootDir/android-common.gradle")
}

android {
    defaultConfig {
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
}

dependencies {
    // Room
    api("androidx.room:room-runtime:${Version.Room}")
    api("androidx.room:room-ktx:${Version.Room}")
    kapt("androidx.room:room-compiler:${Version.Room}")

    // Test
    testImplementation("junit:junit:${Version.JUnit}")

    // Moshi
    implementation("com.squareup.moshi:moshi:${Version.Moshi}")
    implementation("com.squareup.moshi:moshi-kotlin:${Version.Moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Version.Moshi}")

    // Retrofit
    api("com.squareup.retrofit2:retrofit:${Version.Retrofit}")
    api("com.squareup.retrofit2:converter-moshi:${Version.Retrofit}")

    implementation(project(":domain"))
}
