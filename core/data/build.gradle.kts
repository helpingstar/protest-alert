plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.database)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.notifications)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}