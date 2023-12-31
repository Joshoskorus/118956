plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.loginauth"
    compileSdk = 34



    defaultConfig {
        applicationId = "com.example.loginauth"
        minSdk = 24
        targetSdk = 31
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.itextpdf:itext7-core:7.2.1")
    implementation("com.itextpdf:itextpdf:5.5.13")
    implementation ("com.google.firebase:firebase-analytics:21.5.0")
    testImplementation("junit:junit:4.13.2")
    implementation("com.android.volley:volley:1.2.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
