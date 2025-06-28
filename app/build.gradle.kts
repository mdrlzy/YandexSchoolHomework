import java.util.Properties

plugins {
    id("android-app-module")
}

android {
    defaultConfig {
        applicationId = "com.mdrlzy.budgetwise"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        val token = getLocalProps().getProperty("bearer.token") ?: ""
        buildConfigField("String", "BEARER_TOKEN", "\"$token\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(project(":feature:transactions"))
    implementation(project(":feature:account"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:settings"))

    implementation(libs.lottie.compose)

    implementation(libs.ktor.client.core)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

fun getLocalProps(): Properties {
    val props = Properties()
    val localPropsFile = File(rootDir, "local.properties")

    if (localPropsFile.exists()) {
        localPropsFile.inputStream().use { stream ->
            props.load(stream)
        }
    }

    return props
}
