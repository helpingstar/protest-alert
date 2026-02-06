package io.github.helpingstar.protest_alert.core.datastore

import kotlin.time.Instant


data class LastUpdatedAt(
    val protestResourceLastUpdatedAt: Instant = Instant.DISTANT_PAST,
    val regionLastUpdatedAt: Instant = Instant.DISTANT_PAST,
    val announcementLastUpdatedAt: Instant = Instant.DISTANT_PAST
)