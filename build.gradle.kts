plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    // Replace with your actual package namespace if different
    namespace = "com.tv.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tv.app"
        minSdk = 21 // Android 5.0 (Lollipop) - baseline for Android TV armeabi-v7a devices
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Restrict compiled native libraries (.so) strictly to 32-bit ARM
        ndk {
            abiFilters += listOf("armeabi-v7a")
        }
    }

    signingConfigs {
        // Fallback signing config: allows release builds to install on hardware without a production key
        create("localRelease") {
            initWith(getByName("debug"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("localRelease")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        // Prevent packaging conflicting ABIs
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    // Core Android Libraries
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Android TV Leanback Support
    implementation("androidx.leanback:leanback:1.0.0")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
