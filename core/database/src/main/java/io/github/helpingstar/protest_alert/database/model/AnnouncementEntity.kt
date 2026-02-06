package io.github.helpingstar.protest_alert.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.helpingstar.protest_alert.core.model.data.Announcement
import io.github.helpingstar.protest_alert.core.model.data.AnnouncementType
import kotlin.time.Instant

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    @ColumnInfo("start_at")
    val startAt: Instant,
    @ColumnInfo("end_at")
    val endAt: Instant? = null,
    @ColumnInfo("updated_at")
    val updatedAt: Instant,
    @ColumnInfo("is_read")
    val isRead: Boolean = false
)

fun AnnouncementEntity.asExternalModel() = Announcement(
    id = id,
    type = AnnouncementType.fromString(type),
    title = title,
    body = body,
    startAt = startAt,
    endAt = endAt,
    updatedAt = updatedAt,
    isRead = isRead
)
