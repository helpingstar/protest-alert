package io.github.helpingstar.protest_alert.core.network.model

import io.github.helpingstar.protest_alert.core.model.data.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class NetworkRegion(
    val id: Long,
    val name: String,
    @SerialName("created_at") val createdAt: Instant,
)

@OptIn(ExperimentalTime::class)
fun NetworkRegion.asExternalModel(): Region =
    Region(
        id = name,
        name = name,
        createdAt = createdAt,
    )