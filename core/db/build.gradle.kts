plugins {
    id("android-core-module")
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}