plugins {
    id("android-core-module")
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    
    implementation(libs.arrow.core)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}