plugins {
    alias(libs.plugins.protestalert.android.feature.impl)
    alias(libs.plugins.protestalert.android.library.compose)
}

android {
    namespace = "io.github.helpingstar.protest_alert.feature.schedule"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.feature.schedule.api)
}