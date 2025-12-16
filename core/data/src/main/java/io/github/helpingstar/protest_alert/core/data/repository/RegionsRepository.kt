package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.data.Syncable
import io.github.helpingstar.protest_alert.core.model.data.Region
import kotlinx.coroutines.flow.Flow

interface RegionsRepository : Syncable {
    fun getRegions(): Flow<List<Region>>

    fun getRegion(id: String): Flow<Region>
}