plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.android.room)
    alias(libs.plugins.protestalert.hilt)
}

android {
    namespace = "io.github.helpingstar.protest_alert.database"
}



dependencies {
    implementation(projects.core.model)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}