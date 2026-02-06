package io.github.helpingstar.protest_alert.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class NetworkAnnouncement(
    val id: String,
    val type: String,
    val status: String,
    val title: String,
    val body: String,
    @SerialName("start_at") val startAt: Instant,
    @SerialName("end_at") val endAt: Instant? = null,
    @SerialName("updated_at") val updatedAt: Instant
)
