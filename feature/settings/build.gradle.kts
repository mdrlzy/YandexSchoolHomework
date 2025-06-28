plugins {
    id("android-feature-module")
}

android {
    defaultConfig {
        ksp {
            arg("compose-destinations.moduleName", "settings")
        }
    }
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
