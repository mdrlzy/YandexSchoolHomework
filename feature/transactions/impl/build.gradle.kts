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
    implementation(project(":feature:account:api"))
    implementation(project(":feature:categories:api"))
    implementation(project(":feature:transactions:api"))

    implementation(libs.androidx.work.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
