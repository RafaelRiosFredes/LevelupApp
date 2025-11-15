plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.levelup"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.levelup"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation(libs.androidx.ui)
    val roomVersion = "2.6.1"
    val lifecycleVersion = "2.8.6"
    val activityCompose = "1.9.3"
    val navCompose = "2.8.3"

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Convertidor JSON
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp (opcional pero recomendado)
        implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("androidx.activity:activity-compose:$activityCompose")
    implementation("androidx.navigation:navigation-compose:$navCompose")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")

// Para Room Testing
    androidTestImplementation("androidx.room:room-testing:2.6.1")

// Para coroutines test en androidTest
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

// Compose UI testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.3")

}