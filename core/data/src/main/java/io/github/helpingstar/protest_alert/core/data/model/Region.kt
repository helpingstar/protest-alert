package io.github.helpingstar.protest_alert.core.data.model

import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import io.github.helpingstar.protest_alert.database.model.RegionEntity
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun NetworkRegion.asEntity() = RegionEntity(
    id = id,
    name = name,
    createdAt = createdAt
)