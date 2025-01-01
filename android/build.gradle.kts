plugins {
    id(libs.plugins.ksp.get().pluginId)
}

android {
    defaultConfig {
        applicationId = "com.explorer.android"
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    flavorDimensions += "mode"
    productFlavors {
        create("advertiser") {
            dimension = "mode"
            applicationIdSuffix = ".advertiser"
            versionNameSuffix = "-advertiser"
            buildConfigField("boolean", "IS_ADVERTISER", "true")
        }
        create("consumer") {
            dimension = "mode"
            applicationIdSuffix = ".consumer"
            versionNameSuffix = "-consumer"
            buildConfigField("boolean", "IS_ADVERTISER", "false")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))

    implementation(project(":discovery:domain"))
    implementation(project(":discovery:data"))
    implementation(project(":discovery:local"))
    implementation(project(":discovery:ui"))

    implementation(libs.androidx.activity.compose)

    implementation(libs.dagger.android)
    ksp(libs.dagger.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.test.coroutines)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}