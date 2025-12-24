plugins {
    alias(libs.plugins.protestalert.android.feature.impl)
    alias(libs.plugins.protestalert.android.library.compose)
}

android {
    namespace = "io.github.helpingstar.protest_alert.feature.settings"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.feature.settings.api)
}