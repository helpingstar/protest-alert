plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.protestalert.android.library)
}

android {
    namespace = "io.github.helpingstar.protest_alert.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)


    implementation(libs.javax.inject)

    testImplementation(projects.core.testing)

}