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

        kotlinCompilerExtensionVersion = "1.5.3" // ✅ muy importante para Compose

    }



}




dependencies {

    implementation(libs.androidx.ui)
    // ------------------------------

    // 🔹 Versiones

    // ------------------------------

    val roomVersion = "2.6.1"

    val lifecycleVersion = "2.8.6"

    val activityCompose = "1.9.3"

    val navCompose = "2.8.3"



    // ------------------------------

    // 🔹 Jetpack Compose

    // ------------------------------

    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.ui:ui-graphics")

    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.compose.material3:material3:1.3.0")

    debugImplementation("androidx.compose.ui:ui-tooling")



    // 🔹 Íconos Material (para Icons.Default.*)

    implementation("androidx.compose.material:material-icons-core:1.7.2")

    implementation("androidx.compose.material:material-icons-extended:1.7.2")



    // ------------------------------

    // 🔹 Activity + Navigation

    // ------------------------------

    implementation("androidx.activity:activity-compose:$activityCompose")

    implementation("androidx.navigation:navigation-compose:$navCompose")



    // ------------------------------

    // 🔹 Lifecycle / ViewModel / LiveData

    // ------------------------------

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")



    // ------------------------------

    // 🔹 Room (SQLite)

    // ------------------------------

    implementation("androidx.room:room-runtime:$roomVersion")

    implementation("androidx.room:room-ktx:$roomVersion")

    // kapt("androidx.room:room-compiler:$roomVersion") // ⚠️ Solo si activas kapt más adelante



    // ------------------------------

    // 🔹 Coroutines

    // ------------------------------

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")



    // ------------------------------

    // 🔹 Cargar imágenes (Coil)

    // ------------------------------

    implementation("io.coil-kt:coil-compose:2.6.0")



    // ------------------------------

    // 🔹 Retrofit (APIs externas)

    // ------------------------------

    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

    implementation("androidx.navigation:navigation-compose:2.8.3")

    // ------------------------------

    // 🔹 Testing

    // ------------------------------

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-test-manifest")

}

