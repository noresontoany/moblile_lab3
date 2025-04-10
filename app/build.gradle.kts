plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.lab_3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lab_3"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.ui.desktop)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(kotlin("reflect"))
    implementation("org.apache.poi:poi:5.2.3")       // Для работы с .xls файлами
    implementation("org.apache.poi:poi-ooxml:5.2.3") // Для работы с .xlsx файлами
//    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
//    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
//    implementation ("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("androidx.fragment:fragment-ktx:1.8.6")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
}