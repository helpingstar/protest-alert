package io.github.helpingstar.protest_alert.core.model.data

import kotlin.time.Instant


data class Region(
    val id: String,
    val name: String,
    val createdAt: Instant,
)