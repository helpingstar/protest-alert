package io.github.helpingstar.protest_alert.core.data.model

import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import io.github.helpingstar.protest_alert.database.model.RegionEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
    region = region,
    updatedAt = updatedAt
)


@OptIn(ExperimentalTime::class)
fun NetworkProtestResource.regionEntityShells() =
    RegionEntity(
        id = region,
        name = region,
        createdAt = Instant.DISTANT_PAST
    )