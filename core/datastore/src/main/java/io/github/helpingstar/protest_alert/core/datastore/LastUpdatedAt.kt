package io.github.helpingstar.protest_alert.core.datastore

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class LastUpdatedAt(
    val protestResourceLastUpdatedAt: Instant = Instant.DISTANT_PAST,
    val regionLastUpdatedAt: Instant = Instant.DISTANT_PAST
)