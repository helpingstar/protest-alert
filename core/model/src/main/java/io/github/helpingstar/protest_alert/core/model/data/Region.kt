package io.github.helpingstar.protest_alert.core.model.data

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Region(
    val id: Long,
    val name: String,
    val createdAt: Instant,
)