package io.github.helpingstar.protest_alert.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "protest_resources"
)
data class ProtestResourceEntity(
    @PrimaryKey
    val id: Long,
    val date: LocalDate,
    @ColumnInfo("start_at")
    val startAt: Instant? = null,
    @ColumnInfo("end_at")
    val endAt: Instant? = null,
    val location: String? = null,
    val participants: Int? = null,
    @ColumnInfo("additional_info")
    val additionalInfo: JsonObject? = null,
    @ColumnInfo("created_at")
    val createdAt: Instant,
    val region: String
)

@OptIn(ExperimentalTime::class)
fun ProtestResourceEntity.asExternalModel() = ProtestResource(
    id = id,
    date = date,
    startAt = startAt,
    endAt = endAt,
    location = location,
    participants = participants,
    additionalInfo = additionalInfo,
    createdAt = createdAt,
    region = region,
)
