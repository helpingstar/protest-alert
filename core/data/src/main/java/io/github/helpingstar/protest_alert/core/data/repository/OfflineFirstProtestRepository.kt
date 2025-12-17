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
import io.github.helpingstar.protest_alert.database.dao.ProtestResourceDao
import io.github.helpingstar.protest_alert.database.dao.RegionDao
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import io.github.helpingstar.protest_alert.database.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.ExperimentalTime

private const val SYNC_BATCH_SIZE = 10

@OptIn(ExperimentalTime::class)
internal class OfflineFirstProtestRepository @Inject constructor(
    private val paPreferencesDataSource: PaPreferencesDataSource,
    private val protestResourceDao: ProtestResourceDao,
    private val regionDao: RegionDao,
    private val network: PaNetworkDataSource,
//    private val notifier: Notifier
) : ProtestRepository {
    override fun getProtestResources(
        query: ProtestResourceQuery
    ): Flow<List<ProtestResource>> = protestResourceDao.getProtestResources(
        useFilterRegionIds = query.filterRegionIds != null,
        filterRegionIds = query.filterRegionIds ?: emptySet(),
    )
        .map { it.map(ProtestResourceEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        // TODO(hs) have to handle it
        return synchronizer.changeListSync(
            versionReader = LastUpdatedAt::protestResourceLastUpdatedAt,
            changeListFetcher = { lastUpdatedAt ->
                network.getProtestResourceChangeList(after = lastUpdatedAt)
            },
            versionUpdater = { lastUpdatedAt ->
                copy(protestResourceLastUpdatedAt = lastUpdatedAt)
            },
            modelDeleter = protestResourceDao::deleteProtestResources,
            modelUpdater = { changedIds ->
                // TODO(hs) 온보딩, 알림기능 추가

                changedIds.chunked(SYNC_BATCH_SIZE).forEach { chunkedIds ->
                    val networkProtestResources = network.getProtestResources(ids = chunkedIds)

                    regionDao.insertOrIgnoreRegions(
                        regionEntities = networkProtestResources
                            .map(NetworkProtestResource::regionEntityShells)
                    )

                    protestResourceDao.upsertProtestResources(
                        protestResourceEntities = networkProtestResources.map(
                            NetworkProtestResource::asEntity
                        )
                    )
                }
            }

        )
    }
}