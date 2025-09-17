plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.mecofarid.summy"
    compileSdk = libs.versions.compileTargetSdk.get().toInt()
    defaultConfig {
        applicationId = "com.mecofarid.summy"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileTargetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.version.get()
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.composeActivity)
    implementation(libs.material)
}