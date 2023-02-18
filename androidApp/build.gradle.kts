plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.mecofarid.summy"
    compileSdk = libs.versions.compileTargetSdk.get().toInt()
    defaultConfig {
        applicationId = "com.mecofarid.summy.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileTargetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.version.get()
    }

    // TODO: Remove when move to compose
    buildFeatures{
        viewBinding = true
    }
    // TODO: Uncomment after migrating to compose
//    buildFeatures {
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
//    }
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

    // TODO: Delete after migrating to compose
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
}