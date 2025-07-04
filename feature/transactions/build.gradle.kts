plugins {
    id("android-feature-module")
}

android {
    defaultConfig {
        ksp {
            arg("compose-destinations.moduleName", "transactions")
        }
    }
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
