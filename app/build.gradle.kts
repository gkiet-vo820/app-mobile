plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.appbanhang"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appbanhang"
        minSdk = 29
        targetSdk = 36
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //Badge
    implementation("com.nex3z:notification-badge:1.0.4")

    //Event bus
    implementation("org.greenrobot:eventbus:3.2.0")

    //Rxjava
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    //Paper
    implementation("io.github.pilgr:paperdb:2.7.1")

    //Gson
    implementation("com.google.code.gson:gson:2.8.9")

    //Lottie
    implementation("com.airbnb.android:lottie:6.4.0")

    //neumorphism
    implementation("com.github.fornewid:neumorphism:0.3.0")

    //gridlayout
    implementation("androidx.gridlayout:gridlayout:1.0.0")
}