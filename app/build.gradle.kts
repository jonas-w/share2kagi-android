plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kagi.share"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kagi.share"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "2.0"
    }

    buildTypes {

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Share2Kagi (debug)")
            resValue("drawable", "app_icon", "@mipmap/kagi_launcher_debug")
            resValue("drawable", "app_icon_round", "@mipmap/kagi_launcher_debug_round")
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")

            resValue("string", "app_name", "Share2Kagi")
            resValue("drawable", "app_icon", "@mipmap/kagi_launcher_icon")
            resValue("drawable", "app_icon_round", "@mipmap/kagi_launcher_icon_round")
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.compose.preference)
}
tasks.named { name -> name.contains("Test") }.configureEach {
    enabled = false
}