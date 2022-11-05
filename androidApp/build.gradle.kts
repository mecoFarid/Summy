plugins {
    id("com.android.application")
    kotlin("android")
}

val extras = rootProject.extra

android {
    namespace = "com.mecofarid.summy"
    compileSdk = extras["compiledSdk"] as Int
    defaultConfig {
        applicationId = "com.mecofarid.summy"
        minSdk = extras["minSdk"] as Int
        targetSdk = extras["targetSdk"] as Int
        versionCode = 1
        versionName = "1.0"
    }
    viewBinding {
        enable = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}