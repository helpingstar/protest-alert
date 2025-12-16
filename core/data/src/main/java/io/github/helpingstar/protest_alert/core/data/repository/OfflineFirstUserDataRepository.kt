package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource
import io.github.helpingstar.protest_alert.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val paPreferencesDataSource: PaPreferencesDataSource
) : UserDataRepository {
    override val userData: Flow<UserData> =
        paPreferencesDataSource.userData

    override suspend fun setRegionIdFollowed(followedRegionId: String, followed: Boolean) {
        paPreferencesDataSource.setRegionIdFollowed(followedRegionId, followed)
    }
}