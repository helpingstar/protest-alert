package io.github.helpingstar.protest_alert.core.data.repository

import android.util.Log
import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.data.changeListSync
import io.github.helpingstar.protest_alert.core.data.model.asEntity
import io.github.helpingstar.protest_alert.core.datastore.LastUpdatedAt
import io.github.helpingstar.protest_alert.core.model.data.Region
import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import io.github.helpingstar.protest_alert.database.dao.RegionDao
import io.github.helpingstar.protest_alert.database.model.RegionEntity
import io.github.helpingstar.protest_alert.database.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.ExperimentalTime

private const val TAG = "OfflineFirstRegionsRepository"

@OptIn(ExperimentalTime::class)
class OfflineFirstRegionsRepository @Inject constructor(
    private val regionDao: RegionDao,
    private val network: PaNetworkDataSource,
) : RegionsRepository {
    override fun getRegions(): Flow<List<Region>> =
        regionDao.getRegionEntities()
            .map { it.map(RegionEntity::asExternalModel) }

    override fun getRegion(id: Long): Flow<Region> =
        regionDao.getRegionEntity(id).map { it.asExternalModel() }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        Log.d(TAG, "syncWith Called")
        return synchronizer.changeListSync(
            versionReader = LastUpdatedAt::regionLastUpdatedAt,
            changeListFetcher = { lastUpdatedAt ->
                network.getRegionChangeList(after = lastUpdatedAt)
            },
            versionUpdater = { latestVersion ->
                copy(regionLastUpdatedAt = latestVersion)
            },
            modelDeleter = regionDao::deleteRegions,
            modelUpdater = { changedIds ->
                val networkRegions = network.getRegions(ids = changedIds)
                regionDao.upsertRegions(
                    entities = networkRegions.map(NetworkRegion::asEntity)
                )
            }
        )
    }
}