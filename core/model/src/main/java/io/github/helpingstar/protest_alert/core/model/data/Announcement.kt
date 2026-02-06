package io.github.helpingstar.protest_alert.core.model.data

import kotlin.time.Instant

/**
 * Domain model for announcements.
 *
 * @param id Unique identifier (UUID from Supabase)
 * @param type Announcement type (NORMAL, PINNED, EMERGENCY)
 * @param title Announcement title
 * @param body Announcement body text
 * @param startAt When the announcement becomes visible
 * @param endAt When the announcement expires (null = no expiry)
 * @param updatedAt Last update timestamp (used for sync)
 * @param isRead Whether the user has read this announcement (local-only)
 */
data class Announcement(
    val id: String,
    val type: AnnouncementType,
    val title: String,
    val body: String,
    val startAt: Instant,
    val endAt: Instant?,
    val updatedAt: Instant,
    val isRead: Boolean
)

/**
 * Announcement type enum.
 * Order matters for sorting: EMERGENCY > PINNED > NORMAL
 */
enum class AnnouncementType {
    EMERGENCY,
    PINNED,
    NORMAL;

    companion object {
        fun fromString(value: String): AnnouncementType = when (value.lowercase()) {
            "emergency" -> EMERGENCY
            "pinned" -> PINNED
            else -> NORMAL
        }
    }
}
