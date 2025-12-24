plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.android.library.compose)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.ui"
}

dependencies {
    implementation(projects.core.model)
    api(projects.core.designsystem)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}