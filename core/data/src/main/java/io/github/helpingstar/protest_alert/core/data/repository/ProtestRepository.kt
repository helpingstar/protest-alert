package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.data.Syncable
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import kotlinx.coroutines.flow.Flow

data class ProtestResourceQuery(
    val filterRegionIds: Set<String>? = null,
)

interface ProtestRepository : Syncable {
    fun getNewsResources(
        query: ProtestResourceQuery = ProtestResourceQuery(
            filterRegionIds = null,
        )
    ): Flow<List<ProtestResource>>
}