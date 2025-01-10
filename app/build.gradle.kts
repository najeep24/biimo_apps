plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.finpro"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.finpro"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
    }

    sourceSets["main"].jniLibs.srcDirs("src/main/jniLibs")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.lifecycle.viewmodel.android)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.12.0") {
        exclude(group = "com.android.support")
    }

    implementation ("com.google.android.material:material:1.5.0")
    implementation("com.google.code.gson:gson:2.8.7")
    implementation("androidx.core:core:1.13.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")
    implementation("com.google.guava:guava:31.0.1-android")
    implementation("org.reactivestreams:reactive-streams:1.0.4")

    // Retrofit Core Library
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson Converter for JSON Parsing
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp Logging Interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Add the dependency for the Vertex AI in Firebase library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-vertexai")

    // Required for one-shot operations (to use `ListenableFuture` from Guava Android)
    implementation("com.google.guava:guava:31.0.1-android")

    // Required for streaming operations (to use `Publisher` from Reactive Streams)
    implementation("org.reactivestreams:reactive-streams:1.0.4")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0'")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.9")

    implementation ("com.google.firebase:firebase-auth:22.1.0")

    implementation ("com.google.firebase:firebase-database:20.2.3")
}