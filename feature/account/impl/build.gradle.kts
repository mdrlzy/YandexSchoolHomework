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
    implementation(project(":feature:categories:api"))
    implementation(project(":feature:transactions:api"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}