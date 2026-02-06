package io.github.helpingstar.protest_alert.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.helpingstar.protest_alert.database.model.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    /**
     * Get all announcements sorted by type (EMERGENCY > PINNED > NORMAL) then by startAt DESC.
     */
    @Query(
        value = """
            SELECT * FROM announcements
            ORDER BY
                CASE type
                    WHEN 'emergency' THEN 0
                    WHEN 'pinned' THEN 1
                    ELSE 2
                END,
                start_at DESC
        """
    )
    fun getAnnouncements(): Flow<List<AnnouncementEntity>>

    /**
     * Check if there are any unread announcements.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM announcements WHERE is_read = 0)")
    fun hasUnreadAnnouncements(): Flow<Boolean>

    /**
     * Get read status map for preserving during sync.
     */
    @Query("SELECT id, is_read FROM announcements")
    suspend fun getReadStatusList(): List<AnnouncementReadStatus>

    @Upsert
    suspend fun upsertAnnouncements(entities: List<AnnouncementEntity>)

    @Query("UPDATE announcements SET is_read = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE announcements SET is_read = 1")
    suspend fun markAllAsRead()

    @Query("DELETE FROM announcements WHERE id IN (:ids)")
    suspend fun deleteAnnouncements(ids: List<String>)
}

/**
 * Data class for read status query result.
 */
data class AnnouncementReadStatus(
    val id: String,
    @androidx.room.ColumnInfo("is_read")
    val isRead: Boolean
)
