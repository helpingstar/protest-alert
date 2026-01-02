package io.github.helpingstar.protest_alert.core.network.model

import io.github.helpingstar.protest_alert.core.model.data.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant


@Serializable
data class NetworkRegion(
    val id: Long,
    val name: String,
    @SerialName("created_at") val createdAt: Instant,
)


fun NetworkRegion.asExternalModel(): Region =
    Region(
        id = name,
        name = name,
        createdAt = createdAt,
    )