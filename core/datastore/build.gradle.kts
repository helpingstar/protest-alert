plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.datastore"
}

dependencies {
    api(libs.androidx.dataStore)
    api(projects.core.datastoreProto)
    api(projects.core.model)
    implementation(projects.core.common)
}