import com.android.build.gradle.LibraryExtension

plugins {
    id("com.android.library")
    id("android-base")
    id("org.jetbrains.kotlin.plugin.serialization")
}

configure<LibraryExtension> {
    baseAndroidConfig(project)
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.arrow.core)
}