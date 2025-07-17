import com.android.build.gradle.LibraryExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("android-base")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
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
    api(project(":core:db"))

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

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.json)

    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.compiler)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
}