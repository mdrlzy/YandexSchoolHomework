plugins {
    id("android-api-module")
}

dependencies {
    implementation(project(":feature:account:api"))
    implementation(project(":feature:categories:api"))
}