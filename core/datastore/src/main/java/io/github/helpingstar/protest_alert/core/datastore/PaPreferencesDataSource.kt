package io.github.helpingstar.protest_alert.core.datastore

import androidx.datastore.core.DataStore
import io.github.helpingstar.protest_alert.core.model.data.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PaPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data
        .map {
            UserData(
                followedRegions = it.followedRegionIdsMap.keys,
            )
        }
}