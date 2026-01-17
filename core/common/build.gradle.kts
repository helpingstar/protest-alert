plugins {
    alias(libs.plugins.protestalert.jvm.library)
    alias(libs.plugins.protestalert.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
}