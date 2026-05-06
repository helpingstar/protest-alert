import java.util.Properties

plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "io.github.helpingstar.protest_alert.core.network"
    testOptions.unitTests.isIncludeAndroidResources = true

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        buildConfigField("String", "SUPABASE_URL", "\"${localProperties["SUPABASE_URL"]}\"")
        buildConfigField("String", "SUPABASE_KEY", "\"${localProperties["SUPABASE_KEY"]}\"")
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.ktor.client.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}