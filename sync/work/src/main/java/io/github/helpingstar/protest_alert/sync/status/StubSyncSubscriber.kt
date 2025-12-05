package io.github.helpingstar.protest_alert.sync.status

import android.util.Log
import javax.inject.Inject

private const val TAG = "StubSyncSubscriber"

class StubSyncSubscriber @Inject constructor() : SyncSubscriber {
    override suspend fun subscribe() {
        Log.d(TAG, "Subscribing to sync")
    }
}