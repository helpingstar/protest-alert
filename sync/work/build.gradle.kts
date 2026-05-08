plugins {
    alias(libs.plugins.protestalert.android.library)
    alias(libs.plugins.protestalert.hilt)
}


android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    namespace = "io.github.helpingstar.protest_alert.sync"
}

dependencies {
    ksp(libs.hilt.ext.compiler)

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)

    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.common)

    implementation(libs.firebase.cloud.messaging)
    implementation(platform(libs.firebase.bom))

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
