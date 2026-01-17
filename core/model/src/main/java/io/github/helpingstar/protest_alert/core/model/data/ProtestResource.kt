package io.github.helpingstar.protest_alert.core.model.data

import kotlinx.datetime.LocalDate
import kotlin.time.Instant


data class ProtestResource(
    val id: Long,
    val date: LocalDate,
    val startAt: Instant?,
    val endAt: Instant?,
    val location: String?,
    val participants: Int?,
    val additionalInfo: Map<String, String>?,
    val createdAt: Instant,
    val region: String,
    val updatedAt: Instant
)