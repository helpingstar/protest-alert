package io.github.helpingstar.protest_alert.core.testing.repository

import io.github.helpingstar.protest_alert.core.data.repository.UserDataRepository
import io.github.helpingstar.protest_alert.core.model.data.UserData
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

val emptyUserData = UserData(
    followedRegions = emptySet(),
    shouldHideOnboarding = false,
    updateNotificationEnabled = false,
)

class TestUserDataRepository : UserDataRepository {
    private val _userData = MutableSharedFlow<UserData>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val currentUserData get() = _userData.replayCache.firstOrNull() ?: emptyUserData

    override val userData: Flow<UserData> = _userData.filterNotNull()

    fun setFollowedRegionIds(followedRegionIds: Set<String>) {
        _userData.tryEmit(currentUserData.copy(followedRegions = followedRegionIds))
    }

    override suspend fun setRegionIdFollowed(followedRegionId: String, followed: Boolean) {
        currentUserData.let { current ->
            val followedRegions = if (followed) {
                current.followedRegions + followedRegionId
            } else {
                current.followedRegions - followedRegionId
            }

            _userData.tryEmit(current.copy(followedRegions = followedRegions))
        }
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(shouldHideOnboarding = shouldHideOnboarding))
        }
    }

    override suspend fun setUpdateNotificationEnabled(updateNotificationEnabled: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(updateNotificationEnabled = updateNotificationEnabled))
        }
    }

    fun setUserData(userData: UserData) {
        _userData.tryEmit(userData)
    }
}
