package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource
import javax.inject.Inject

internal class OfflineFirstProtestRepository @Inject constructor(
    private val paPreferencesDataSource: PaPreferencesDataSource,

    ) {
}