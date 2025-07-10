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
    implementation(libs.kotlinx.serialization.core)
}