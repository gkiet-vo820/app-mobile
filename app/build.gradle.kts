plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // kết nối với google (firebase)
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
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Badge
    implementation("com.nex3z:notification-badge:1.0.4")

    // Event bus
    implementation("org.greenrobot:eventbus:3.2.0")

    // Rxjava
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.akarnokd:rxjava3-retrofit-adapter:3.0.0")

    // Paper
    implementation("io.github.pilgr:paperdb:2.7.1")

    // Gson
    implementation("com.google.code.gson:gson:2.8.9")

    // Lottie
    implementation("com.airbnb.android:lottie:6.4.0")

    // Neumorphism
    implementation("com.github.fornewid:neumorphism:0.3.0")

    // Gridlayout
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging:23.4.0")

    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database:20.3.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // TabLayout
    implementation("com.google.android.material:material:1.9.0")

    // Chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Places (gg map)
//    implementation("com.google.android.libraries.places:places:3.3.0")

    // Momo
//    implementation("com.github.momocit:momosdk:1.0.7")
    //implementation(files("libs/momosdk-1.0.7.aar"))
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
//    implementation(name = "momosdk-1.0.7", ext = "aar")
}