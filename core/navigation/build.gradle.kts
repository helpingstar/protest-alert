plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
    alias(libs.plugins.compose)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.navigation"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
}