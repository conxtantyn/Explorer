plugins {
    id(libs.plugins.ksp.get().pluginId)
    id(libs.plugins.parcelise.get().pluginId)
}

android {
    buildFeatures {
        buildConfig = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    defaultConfig {
        buildConfigField("String", "UUID", "\"0000180A-0000-1000-8000-00805F9B34FB\"")
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":discovery:domain"))

    implementation(libs.dagger.android)
    ksp(libs.dagger.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.test.coroutines)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
