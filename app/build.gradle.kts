plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.kspPlugin)
}

android {
    namespace = "com.xcvi.openfoodfactsapi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.xcvi.openfoodfactsapi"
        minSdk = 29
        targetSdk = 35
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
}

dependencies {
    implementation ("com.google.accompanist:accompanist-permissions:0.34.0")

    implementation ("com.google.mlkit:text-recognition:16.0.0")
    implementation ("androidx.camera:camera-camera2:1.2.2")
    implementation ("androidx.camera:camera-lifecycle:1.2.2")
    implementation ("androidx.camera:camera-view:1.2.2")
    implementation ("com.google.mlkit:barcode-scanning:17.1.0")

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.hilt.androidcompiler)
    ksp(libs.hilt.hiltcompiler)

    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.compose.material3:material3:1.3.2") // Or newer
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.androidx.navigation)

    implementation ("io.ktor:ktor-client-core:1.6.3")
    implementation ("io.ktor:ktor-client-android:1.6.3")
    implementation ("io.ktor:ktor-client-serialization:1.6.3")
    implementation ("io.ktor:ktor-client-logging:1.6.3")
    implementation ("ch.qos.logback:logback-classic:1.2.3")

    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
}