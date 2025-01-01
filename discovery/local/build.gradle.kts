
android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("int", "MANUFACTURER", "3309")
        buildConfigField("boolean", "CONNECTABLE", "false")
    }
}

dependencies {
    implementation(project(":discovery:domain"))
    implementation(project(":discovery:data"))

    implementation(libs.dagger)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.test.coroutines)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
