plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.datastore"
}

dependencies {
    implementation(libs.androidx.dataStore)
    implementation(projects.core.datastoreProto)
    implementation(projects.core.model)
    implementation(projects.core.common)
}