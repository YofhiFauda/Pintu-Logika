plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)

}

android {
    namespace = "com.digitallogic.core_data"
    compileSdk = 35

    defaultConfig {

        minSdk = 27
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "IMAGEKIT_PUBLIC_KEY", "\"${project.findProperty("imagekitPublicKey")}\"")
        buildConfigField("String", "IMAGEKIT_PRIVATE_KEY", "\"${project.findProperty("imagekitPrivateKey")}\"")

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
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.logging.interceptor)
    implementation(platform { libs.firebase.bom })


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}