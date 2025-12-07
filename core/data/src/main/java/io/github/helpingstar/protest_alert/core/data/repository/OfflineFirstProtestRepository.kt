package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.data.model.asEntity
import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.database.dao.ProtestResourceDao
import io.github.helpingstar.protest_alert.database.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val SYNC_BATCH_SIZE = 10

internal class OfflineFirstProtestRepository @Inject constructor(
    private val paPreferencesDataSource: PaPreferencesDataSource,
    private val protestResourceDao: ProtestResourceDao,
    private val network: PaNetworkDataSource,
//    private val notifier: Notifier
) : ProtestRepository {
    override fun getNewsResources(query: ProtestResourceQuery): Flow<List<ProtestResource>> =
        protestResourceDao.getProtestResources()
            .map { it.map { it.asExternalModel() } }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        // TODO(hs) have to handle it
//        var isFirstSync = false
//        return synchronizer.changeListSync(
//            versionReader = ChangeListVersions::protestResourceVersion,
//            changeListFetcher = { currentVersion ->
//                isFirstSync = currentVersion <= 0
//                network.getProtestResourceChangeList(after = currentVersion)
//            },
//            versionUpdater = { latestVersion ->
//                copy(protestResourceVersion = latestVersion)
//            },
//            modelDeleter = protestResourceDao::deleteProtestResources,
//            modelUpdater = { changedIds ->
//                changedIds.chunked(SYNC_BATCH_SIZE).forEach { chunkedIds ->
//                    val networkProtestResources = network.getProtestResources(ids = chunkedIds)
//
//                    protestResourceDao.upsertProtestResources(
//                        protestResourceEntities = networkProtestResources.map(
//                            NetworkProtestResource::asEntity
//                        )
//                    )
//                }
//            }
//
//        )
        return try {
            val networkProtestResources = network.getProtestResources()

            protestResourceDao.upsertProtestResources(
                protestResourceEntities = networkProtestResources.map(
                    NetworkProtestResource::asEntity
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}