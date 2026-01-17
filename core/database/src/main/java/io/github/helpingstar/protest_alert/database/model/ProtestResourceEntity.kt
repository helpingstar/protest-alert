package io.github.helpingstar.protest_alert.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

import kotlin.time.Instant


@Entity(
    tableName = "protest_resources",
    foreignKeys = [
        ForeignKey(
            entity = RegionEntity::class,
            parentColumns = ["id"],
            childColumns = ["region"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["region"])]
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
    val region: String,
    val updatedAt: Instant
)


fun ProtestResourceEntity.asExternalModel() = ProtestResource(
    id = id,
    date = date,
    startAt = startAt,
    endAt = endAt,
    location = location,
    participants = participants,
    additionalInfo = additionalInfo?.toStringMap(),
    createdAt = createdAt,
    region = region,
    updatedAt = updatedAt
)

private fun JsonObject.toStringMap(): Map<String, String> =
    mapValues { (_, value) ->
        (value as? JsonPrimitive)?.content ?: value.toString()
    }