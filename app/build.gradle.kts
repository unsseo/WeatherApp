import java.util.Properties

fun getEnvProperties(): Properties {
    val envFile = rootProject.file(".env")
    val props = Properties()
    if (envFile.exists()) {
        props.load(envFile.inputStream())
    }
    return props
}

val env = getEnvProperties()

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.basicweatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.basicweatherapp"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 최신 버전으로 업데이트
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.0.0") // 11.1.0 → 12.0.0
    implementation("com.squareup.retrofit2:retrofit:2.11.0")         // 2.9.0 → 2.11.0
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")   // 2.9.0 → 2.11.0
    implementation("com.squareup.okhttp3:okhttp:4.12.0")             // 4.9.3 → 4.12.0
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")// 4.9.3 → 4.12.0

    implementation("com.google.android.gms:play-services-location:21.0.1")

}
