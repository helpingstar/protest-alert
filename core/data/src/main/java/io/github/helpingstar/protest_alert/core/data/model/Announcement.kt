package io.github.helpingstar.protest_alert.core.data.model

import io.github.helpingstar.protest_alert.core.network.model.NetworkAnnouncement
import io.github.helpingstar.protest_alert.database.model.AnnouncementEntity

/**
 * Converts NetworkAnnouncement to AnnouncementEntity.
 *
 * @param existingIsRead The existing read status to preserve during sync.
 *                       Defaults to false for new announcements.
 */
fun NetworkAnnouncement.asEntity(existingIsRead: Boolean = false) = AnnouncementEntity(
    id = id,
    type = type,
    title = title,
    body = body,
    startAt = startAt,
    endAt = endAt,
    updatedAt = updatedAt,
    isRead = existingIsRead
)
