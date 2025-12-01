package io.github.helpingstar.protest_alert.core.network.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Protest(
    val id: Long,
    val date: String? = null,
    @SerialName("start_at") val startAt: String? = null,
    @SerialName("end_at") val endAt: String? = null,
    val location: String? = null,
    val participants: Int? = null,
    @SerialName("additional_info") val additionalInfo: JsonObject? = null,
    @SerialName("created_at") val createdAt: String? = null,
    val region: String? = null
)