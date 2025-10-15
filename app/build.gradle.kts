plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.secrets.gradle.plugin)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ben.lincride"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ben.lincride"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    
    // Explicitly set JVM target for all Kotlin compilation tasks
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
}

dependencies {
    // Core modules
    implementation(projects.core.designsystem)
    
    // Core Android
    implementation(libs.androidx.core.ktx.v1170)
    implementation(libs.androidx.lifecycle.runtime.ktx.v294)
    implementation(libs.androidx.activity.compose.v1110)
    
    // Compose BOM
    implementation(platform(libs.compose.bom.v20251000))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose.v130)

    // Navigation
    implementation(libs.androidx.navigation.compose.v295)
    
    // Google Maps Compose
    implementation(libs.maps.compose.v441)
    implementation(libs.play.services.maps.v1920)

    // ViewModel & Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose.v294)
    implementation(libs.androidx.lifecycle.runtime.compose.v294)

    // Testing - Updated professional versions
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    
    // Android Testing - Updated versions
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v370)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    
    // Compose Testing
    androidTestImplementation(platform(libs.compose.bom.v20251000))
    androidTestImplementation(libs.ui.test.junit4)
    
    // Hilt Testing
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    
    // Debug implementations
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}

