@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.jasmeet.downloadmanger"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jasmeet.downloadmanger"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
                resources.excludes.add("META-INF/*")

        }
    }
    testOptions { packagingOptions { jniLibs { useLegacyPackaging = true } } }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    implementation(project(mapOf("path" to ":downloadManager")))

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.media3Ui)
    implementation(libs.media3Exoplayer)

    implementation (libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation (libs.media3Dash)

    implementation (libs.livedata)

    implementation(libs.lottie)

    androidTestImplementation (libs.androidx.core.testing)
    androidTestImplementation (libs.androidx.navigation.testing)
////    implementation ( "com.github.jasi05072001:DownloadManger:1.0")
//    androidTestImplementation ("io.mockk:mockk-android:1.13.8")
//    androidTestImplementation ("io.mockk:mockk-agent:1.13.8")
//
//    testImplementation ("org.mockito:mockito-core:2.19.0")

    val mockk_version = "1.13.8"
    testImplementation ("io.mockk:mockk:$mockk_version")
    testImplementation ("io.mockk:mockk-agent:$mockk_version")
    androidTestImplementation ("io.mockk:mockk-android:$mockk_version")
    androidTestImplementation ("io.mockk:mockk-agent:$mockk_version")





}