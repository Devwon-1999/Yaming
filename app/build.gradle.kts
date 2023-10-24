plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.yaming"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.yaming"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //서버연결시 필요한 종속성 추가
    //MySQL 데이터베이스 연결을 위해 JDBC 드라이버를 Gradle 파일에 추가합니다.
    //build.gradle (Module: app) 파일에 다음 종속성을 추가합니다:
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0") // 코루틴
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")                  // Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")            // Gson 컨버터

    //달력
    implementation ("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.2.1")


    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
}