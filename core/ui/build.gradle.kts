plugins {
    id(libs.plugins.ksp.get().pluginId)
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    api(libs.core.ktx)
    api(libs.appcompat)
    api(libs.material)
    api(libs.viewmodel)
    api(libs.androidx.lifecyle)

    api(libs.dagger.android)

    api(libs.compose.ui)
    api(libs.compose.ui.graphics)
    api(libs.compose.ui.tooling.preview)
    api(libs.compose.material3)
    api(libs.compose.viewmodel)
    api(libs.compose.livedata)

    ksp(libs.dagger.compiler)
}
