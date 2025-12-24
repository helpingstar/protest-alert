plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.android.library.compose)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
}