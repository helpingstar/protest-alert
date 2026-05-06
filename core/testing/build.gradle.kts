plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.testing"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)
    api(projects.core.notifications)
    api(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.datetime)
}
