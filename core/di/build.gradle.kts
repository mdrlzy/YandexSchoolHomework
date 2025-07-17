plugins {
    id("android-core-module")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:db"))
    api(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.ktor.client.core)

    implementation(libs.androidx.room.runtime)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
