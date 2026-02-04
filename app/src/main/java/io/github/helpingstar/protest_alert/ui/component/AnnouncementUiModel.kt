package io.github.helpingstar.protest_alert.ui.component

/**
 * UI model for announcements displayed in AnnouncementBottomSheet.
 *
 * @param id Unique identifier for the announcement
 * @param status The notification status type (NORMAL, EMERGENCY, PINNED)
 * @param title The announcement title
 * @param body The announcement body text
 * @param date The formatted date string (e.g., "2024.07.26")
 * @param isRead Whether the announcement has been read
 */
data class AnnouncementUiModel(
    val id: String,
    val status: NotificationStatus,
    val title: String,
    val body: String,
    val date: String,
    val isRead: Boolean
)
