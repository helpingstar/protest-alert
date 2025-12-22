package io.github.helpingstar.protest_alert.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import io.github.helpingstar.protest_alert.core.model.data.UserData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private const val TAG = "NiaPreferences"

@OptIn(ExperimentalTime::class)
class PaPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data
        .map {
            UserData(
                followedRegions = it.followedRegionIdsMap.keys,
                shouldHideOnboarding = it.shouldHideOnboarding,
            )
        }

    suspend fun setRegionIdFollowed(regionId: String, followed: Boolean) {
        try {
            userPreferences.updateData {
                it.copy {
                    if (followed) {
                        followedRegionIds.put(regionId, true)
                    } else {
                        followedRegionIds.remove(regionId)
                    }
                    updateShouldHideOnboardingIfNecessary()
                }
            }
        } catch (ioException: IOException) {
            Log.e(TAG, "Failed to update user preferences", ioException)
        }
    }

    suspend fun getChangeListVersions() = userPreferences.data
        .map {
            LastUpdatedAt(
                regionLastUpdatedAt = Instant.parse(
                    it.regionLastUpdatedAt.ifEmpty { "1970-01-01T00:00:00Z" }
                ),
                protestResourceLastUpdatedAt = Instant.parse(
                    it.protestResourceLastUpdatedAt.ifEmpty { "1970-01-01T00:00:00Z" }
                )
            )
        }
        .firstOrNull() ?: LastUpdatedAt()

    suspend fun updateChangeListVersion(update: LastUpdatedAt.() -> LastUpdatedAt) {
        try {
            userPreferences.updateData { currentPreferences ->
                val updatedLastUpdatedAt = update(
                    LastUpdatedAt(
                        regionLastUpdatedAt = Instant.parse(currentPreferences.regionLastUpdatedAt.ifEmpty { "1970-01-01T00:00:00Z" }),
                        protestResourceLastUpdatedAt = Instant.parse(currentPreferences.protestResourceLastUpdatedAt.ifEmpty { "1970-01-01T00:00:00Z" }),
                    )
                )

                currentPreferences.copy {
                    regionLastUpdatedAt = updatedLastUpdatedAt.regionLastUpdatedAt.toString()
                    protestResourceLastUpdatedAt =
                        updatedLastUpdatedAt.protestResourceLastUpdatedAt.toString()
                }
            }
        } catch (ioException: IOException) {
            Log.e(TAG, "Failed to update user preferences", ioException)
        }
    }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy { this.shouldHideOnboarding = shouldHideOnboarding }
        }
    }
}

private fun UserPreferencesKt.Dsl.updateShouldHideOnboardingIfNecessary() {
    if (followedRegionIds.isEmpty()) {
        shouldHideOnboarding = false
    }
}