package io.github.helpingstar.protest_alert.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

import kotlin.time.Instant


@Serializable
data class NetworkProtestResource(
    val id: Long,
    val date: LocalDate,
    @SerialName("start_at") val startAt: Instant? = null,
    @SerialName("end_at") val endAt: Instant? = null,
    val location: String? = null,
    val participants: Int? = null,
    @SerialName("additional_info") val additionalInfo: JsonObject? = null,
    @SerialName("created_at") val createdAt: Instant,
    val region: String,
    @SerialName("updated_at") val updatedAt: Instant
)