package io.github.helpingstar.protest_alert.data.repository

import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.datastore.LastUpdatedAt
import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource

class TestSynchronizer(
    private val paPreferences: PaPreferencesDataSource,
) : Synchronizer {
    override suspend fun getLastUpdatedAt(): LastUpdatedAt =
        paPreferences.getChangeListVersions()

    override suspend fun updateChangeListVersions(update: LastUpdatedAt.() -> LastUpdatedAt) =
        paPreferences.updateChangeListVersion(update)
}