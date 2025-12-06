package io.github.helpingstar.protest_alert.core.notifications

import io.github.helpingstar.protest_alert.core.model.data.ProtestResource

interface Notifier {
    fun postProtestNotifications(protestResources: List<ProtestResource>)
}