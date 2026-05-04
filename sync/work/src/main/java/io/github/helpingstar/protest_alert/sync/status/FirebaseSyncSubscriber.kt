package io.github.helpingstar.protest_alert.sync.status

import com.google.firebase.messaging.FirebaseMessaging
import io.github.helpingstar.protest_alert.sync.initializers.SYNC_TOPIC
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class FirebaseSyncSubscriber @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) : SyncSubscriber {
    override suspend fun subscribe() {
        firebaseMessaging
            .subscribeToTopic(SYNC_TOPIC)
            .await()
    }
}