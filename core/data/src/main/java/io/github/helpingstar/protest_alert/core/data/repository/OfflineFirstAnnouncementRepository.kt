package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.data.changeListSync
import io.github.helpingstar.protest_alert.core.data.model.asEntity
import io.github.helpingstar.protest_alert.core.datastore.LastUpdatedAt
import io.github.helpingstar.protest_alert.core.model.data.Announcement
import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.database.dao.AnnouncementDao
import io.github.helpingstar.protest_alert.database.model.AnnouncementEntity
import io.github.helpingstar.protest_alert.database.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class OfflineFirstAnnouncementRepository @Inject constructor(
    private val announcementDao: AnnouncementDao,
    private val network: PaNetworkDataSource,
) : AnnouncementRepository {

    override fun getAnnouncements(): Flow<List<Announcement>> =
        announcementDao.getAnnouncements()
            .map { entities -> entities.map(AnnouncementEntity::asExternalModel) }

    override fun hasUnreadAnnouncements(): Flow<Boolean> =
        announcementDao.hasUnreadAnnouncements()

    override suspend fun markAsRead(id: String) {
        announcementDao.markAsRead(id)
    }

    override suspend fun markAllAsRead() {
        announcementDao.markAllAsRead()
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeListSync(
            versionReader = LastUpdatedAt::announcementLastUpdatedAt,
            changeListFetcher = { lastUpdatedAt ->
                network.getAnnouncementChangeList(after = lastUpdatedAt)
            },
            versionUpdater = { lastUpdatedAt ->
                copy(announcementLastUpdatedAt = lastUpdatedAt)
            },
            modelDeleter = announcementDao::deleteAnnouncements,
            modelUpdater = { changedIds ->
                // Get existing read status to preserve during upsert
                val existingReadStatus = announcementDao.getReadStatusList()
                    .associate { it.id to it.isRead }

                val networkAnnouncements = network.getAnnouncements(ids = changedIds)

                val entities = networkAnnouncements.map { announcement ->
                    announcement.asEntity(
                        existingIsRead = existingReadStatus[announcement.id] ?: false
                    )
                }

                announcementDao.upsertAnnouncements(entities)
            }
        )
}
