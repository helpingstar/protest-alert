package io.github.helpingstar.protest_alert

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.helpingstar.protest_alert.sync.initializers.Sync

@HiltAndroidApp
class PaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Sync.initialize(context = this)
    }
}