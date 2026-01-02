package io.github.helpingstar.protest_alert.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.helpingstar.protest_alert.core.model.data.Region
import kotlin.time.Instant


@Entity(
    tableName = "regions"
)
data class RegionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo("created_at")
    val createdAt: Instant,
)

fun RegionEntity.asExternalModel() = Region(
    id = id,
    name = name,
    createdAt = createdAt
)