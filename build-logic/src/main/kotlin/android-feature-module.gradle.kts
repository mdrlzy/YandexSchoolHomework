import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("android-base")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

configure<LibraryExtension> {
    baseAndroidConfig(project)
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(project(":core:domain"))
    api(project(":core:ui"))
    api(project(":core:network"))
    api(project(":core:di"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.orbit.compose)
    implementation(libs.orbit.viewmodel)

    implementation(libs.arrow.core)

    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.compiler)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
}