plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fi.dy.ose.mobilecomputing"
    compileSdk = sdk.compile

    defaultConfig {
        applicationId = "fi.dy.ose.mobilecomputing"
        minSdk = sdk.min
        targetSdk = sdk.target
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-database"))
    implementation(project(":core-domain"))
    implementation(project(":core-worker"))

    implementation(androidx.core.ktx)
    implementation(androidx.lifecycle.runtimeKtx)
    implementation(androidx.activity.compose)
    implementation(androidx.compose.ui.ui)
    implementation(androidx.compose.ui.tooling)
    implementation(androidx.compose.material)
    implementation(androidx.room.ktx)
    implementation(androidx.room.common)
    implementation(androidx.room.runtime)
    implementation(androidx.biometric.biometric)
    implementation(androidx.work.ktx)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.1")

    implementation(androidx.navigation.hilt.compose)
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    // Accompanist
    implementation("com.google.accompanist:accompanist-insets:0.23.1")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.3")
    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    // Foundation
    implementation("androidx.compose.foundation:foundation:1.3.1")
}

kapt {
    correctErrorTypes = true
}