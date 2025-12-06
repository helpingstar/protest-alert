package io.github.helpingstar.protest_alert.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import io.github.helpingstar.protest_alert.core.model.data.UserData
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val TAG = "NiaPreferences"

class PaPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data
        .map {
            UserData(
                followedRegions = it.followedRegionIdsMap.keys,
            )
        }

    suspend fun setRegionIdFollowed(topicId: String, followed: Boolean) {
        try {
            userPreferences.updateData {
                it.copy {
                    if (followed) {
                        followedRegionIds.put(topicId, true)
                    } else {
                        followedRegionIds.remove(topicId)
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e(TAG, "Failed to update user preferences", ioException)
        }
    }

    suspend fun getChangeListVersions() = userPreferences.data
        .map {
            ChangeListVersions(
                protestResourceVersion = it.protestResourceChangeListVersion
            )
        }
        .firstOrNull() ?: ChangeListVersions()

    suspend fun updateChangeListVersion(update: ChangeListVersions.() -> ChangeListVersions) {
        try {
            userPreferences.updateData { currentPreferences ->
                val updatedChangeListVersions = update(
                    ChangeListVersions(
                        protestResourceVersion = currentPreferences.protestResourceChangeListVersion
                    )
                )

                currentPreferences.copy {
                    protestResourceChangeListVersion =
                        updatedChangeListVersions.protestResourceVersion
                }
            }
        } catch (ioException: IOException) {
            Log.e(TAG, "Failed to update user preferences", ioException)
        }
    }
}