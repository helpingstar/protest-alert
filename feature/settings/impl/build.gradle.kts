plugins {
    alias(libs.plugins.protestalert.android.feature.impl)
    alias(libs.plugins.protestalert.android.library.compose)
}

android {
    namespace = "io.github.helpingstar.protest_alert.feature.settings"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.feature.settings.api)
}
