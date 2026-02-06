package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.data.Syncable
import io.github.helpingstar.protest_alert.core.model.data.Announcement
import kotlinx.coroutines.flow.Flow

/**
 * Repository for announcements.
 * Implements offline-first pattern with local read status tracking.
 */
interface AnnouncementRepository : Syncable {

    /**
     * Get all announcements sorted by type (EMERGENCY > PINNED > NORMAL) then by startAt DESC.
     */
    fun getAnnouncements(): Flow<List<Announcement>>

    /**
     * Check if there are any unread announcements.
     * Used to display notification badge indicator.
     */
    fun hasUnreadAnnouncements(): Flow<Boolean>

    /**
     * Mark a specific announcement as read.
     */
    suspend fun markAsRead(id: String)

    /**
     * Mark all announcements as read.
     */
    suspend fun markAllAsRead()
}
