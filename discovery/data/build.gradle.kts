
dependencies {
    implementation(project(":discovery:domain"))

    implementation(libs.dagger)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.test.coroutines)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.jvm)
}
