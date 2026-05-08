package io.github.helpingstar.protest_alert.core.testing.notifications

import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.notifications.Notifier

class TestNotifier : Notifier {
    private val mutableAddedProtestResources = mutableListOf<List<ProtestResource>>()

    val addedProtestResources: List<List<ProtestResource>> = mutableAddedProtestResources

    override fun postProtestNotifications(protestResources: List<ProtestResource>) {
        mutableAddedProtestResources.add(protestResources)
    }
}