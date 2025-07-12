plugins {
    id("android-feature-module")
}

android {
    defaultConfig {
        ksp {
            arg("compose-destinations.moduleName", "account")
        }
    }
}

dependencies {
    implementation(project(":feature:account:api"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}