package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setRegionIdFollowed(followedRegionId: Long, followed: Boolean)
}