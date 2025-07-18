plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.compose.plugin)
    implementation(libs.ksp.plugin)
    implementation(libs.detekt.plugin)
    implementation(libs.kotlin.serialization.plugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}