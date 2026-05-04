package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.data.changeListSync
import io.github.helpingstar.protest_alert.core.data.model.asEntity
import io.github.helpingstar.protest_alert.core.data.model.regionEntityShells
import io.github.helpingstar.protest_alert.core.datastore.LastUpdatedAt
import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.core.notifications.Notifier
import io.github.helpingstar.protest_alert.database.dao.ProtestResourceDao
import io.github.helpingstar.protest_alert.database.dao.RegionDao
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import io.github.helpingstar.protest_alert.database.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import kotlin.time.Instant

private val FIRST_SYNC_CUTOFF = Instant.parse("1970-01-01T00:00:00Z")
private const val SYNC_BATCH_SIZE = 10

internal class OfflineFirstProtestRepository @Inject constructor(
    private val paPreferencesDataSource: PaPreferencesDataSource,
    private val protestResourceDao: ProtestResourceDao,
    private val regionDao: RegionDao,
    private val network: PaNetworkDataSource,
    private val notifier: Notifier,
) : ProtestRepository {
    override fun getProtestResources(
        query: ProtestResourceQuery,
    ): Flow<List<ProtestResource>> = protestResourceDao.getProtestResources(
        useFilterRegionIds = query.filterRegionIds != null,
        filterRegionIds = query.filterRegionIds ?: emptySet(),
        useFilterSinceDate = query.filterSinceDate != null,
        sinceDate = query.filterSinceDate ?: LocalDate.fromEpochDays(0),
    )
        .map { it.map(ProtestResourceEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        var isFirstSync = false
        return synchronizer.changeListSync(
            versionReader = LastUpdatedAt::protestResourceLastUpdatedAt,
            changeListFetcher = { lastUpdatedAt ->
                isFirstSync = lastUpdatedAt <= FIRST_SYNC_CUTOFF
                network.getProtestResourceChangeList(after = lastUpdatedAt)
            },
            versionUpdater = { lastUpdatedAt ->
                copy(protestResourceLastUpdatedAt = lastUpdatedAt)
            },
            modelDeleter = protestResourceDao::deleteProtestResources,
            modelUpdater = { changedIds ->
                val changedProtestResourceIds = changedIds
                    .mapNotNull(String::toLongOrNull)
                    .toSet()

                val userData = paPreferencesDataSource.userData.first()
                val hasOnboarded = userData.shouldHideOnboarding
                val followedRegionIds = userData.followedRegions
                val shouldPostNotifications = userData.updateNotificationEnabled && hasOnboarded

                val existingProtestResourceIdsThatHaveChanged = when {
                    shouldPostNotifications -> protestResourceDao.getProtestResourceIds(
                        useFilterRegionIds = true,
                        filterRegionIds = followedRegionIds,
                        useFilterProtestResourceIds = true,
                        filterProtestResourceIds = changedProtestResourceIds
                    )
                        .first()
                        .toSet()

                    else -> emptySet()
                }

                changedIds.chunked(SYNC_BATCH_SIZE).forEach { chunkedIds ->
                    val networkProtestResources = network.getProtestResources(ids = chunkedIds)

                    regionDao.insertOrIgnoreRegions(
                        regionEntities = networkProtestResources
                            .map(NetworkProtestResource::regionEntityShells)
                    )

                    protestResourceDao.upsertProtestResources(
                        protestResourceEntities = networkProtestResources.map(
                            NetworkProtestResource::asEntity
                        ),
                    )
                }

                if (shouldPostNotifications && !isFirstSync) {
                    val addedProtestResourceIds =
                        changedProtestResourceIds - existingProtestResourceIdsThatHaveChanged

                    val addedProtestResources = protestResourceDao.getProtestResources(
                        useFilterRegionIds = true,
                        filterRegionIds = followedRegionIds,
                        useFilterProtestResourceIds = true,
                        filterProtestResourceIds = addedProtestResourceIds,
                    )
                        .first()
                        .map(ProtestResourceEntity::asExternalModel)

                    if (addedProtestResources.isNotEmpty()) {
                        notifier.postProtestNotifications(
                            protestResources = addedProtestResources,
                        )
                    }
                }
            }

        )
    }
}
