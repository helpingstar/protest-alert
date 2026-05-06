plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(projects.core.common)
    api(projects.core.datastore)
    api(projects.core.database)
    api(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.notifications)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.core.datastoreTest)
    testImplementation(projects.core.testing)
}