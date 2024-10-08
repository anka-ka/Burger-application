import org.jetbrains.kotlin.daemon.client.KotlinCompilerClient.compile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlin-kapt")

}

android {
    namespace = "com.example.burgerapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.burgerapplication"
        minSdk = 24
        targetSdk = 34
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
        viewBinding = true

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation (libs.retrofit.v290)
    implementation (libs.converter.gson.v290)
    implementation (libs.kotlinx.coroutines.core.v160)
    implementation (libs.kotlinx.coroutines.android.v160)
    implementation ("com.github.bumptech.glide:glide:5.0.0-rc01")

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation ("com.google.dagger:hilt-android:2.48")
    implementation(libs.androidx.espresso.core)
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler.v270alpha09)
    kapt ("com.google.dagger:hilt-compiler:2.48")
    implementation (libs.numberpicker)
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.androidx.constraintlayout)
    implementation (libs.material)
    implementation (libs.androidx.appcompat)
    implementation (libs.androidx.gridlayout)
    implementation (libs.gson)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}