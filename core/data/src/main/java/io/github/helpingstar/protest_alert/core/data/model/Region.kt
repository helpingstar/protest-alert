package io.github.helpingstar.protest_alert.core.data.model

import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import io.github.helpingstar.protest_alert.database.model.RegionEntity


fun NetworkRegion.asEntity() = RegionEntity(
    id = name,
    name = name,
    createdAt = createdAt
)
