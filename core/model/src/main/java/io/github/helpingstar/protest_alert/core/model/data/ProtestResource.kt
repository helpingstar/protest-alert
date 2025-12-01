package io.github.helpingstar.protest_alert.core.model.data

import kotlinx.serialization.json.JsonObject
import java.time.Instant
import java.time.LocalDate

data class ProtestResource(
    val id: Long,
    val date: LocalDate,
    val startAt: Instant?,
    val endAt: Instant?,
    val location: String,
    val participants: Int,
    val additionalInfo: JsonObject?,
    val createdAt: Instant,
    val region: String,
)