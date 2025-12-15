package io.github.helpingstar.protest_alert.core.model.data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class ProtestResource(
    val id: Long,
    val date: LocalDate,
    val startAt: Instant?,
    val endAt: Instant?,
    val location: String?,
    val participants: Int?,
    val additionalInfo: JsonObject?,
    val createdAt: Instant,
    val region: String,
    val updatedAt: Instant
)