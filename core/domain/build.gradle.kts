plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.domain"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
}