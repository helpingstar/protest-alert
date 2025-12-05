package io.github.helpingstar.protest_alert.sync.status

interface SyncSubscriber {
    suspend fun subscribe()
}