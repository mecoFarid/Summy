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

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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
    implementation(platform(libs.composeBom))
    implementation(libs.composeUi)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial3)
    implementation(libs.composeActivity)
    implementation(libs.navigation)
    implementation(libs.composeRuntime)
    implementation(libs.material)
    implementation(libs.accompanist)

    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiToolingPreview)
}