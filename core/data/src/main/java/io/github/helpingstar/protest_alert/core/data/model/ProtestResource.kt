package io.github.helpingstar.protest_alert.core.data.model

import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun NetworkProtestResource.asEntity() = ProtestResourceEntity(
    id = id,
    date = date,
    startAt = startAt,
    endAt = endAt,
    location = location,
    participants = participants,
    additionalInfo = additionalInfo,
    createdAt = createdAt,
    region = region
)