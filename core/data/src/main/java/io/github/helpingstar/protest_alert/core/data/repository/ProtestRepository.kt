package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.data.Syncable
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

data class ProtestResourceQuery(
    val filterRegionIds: Set<String>? = null,
    val filterNewsIds: Set<String>? = null,
    val filterSinceDate: LocalDate? = null,
)

interface ProtestRepository : Syncable {
    fun getProtestResources(
        query: ProtestResourceQuery = ProtestResourceQuery(
            filterRegionIds = null,
            filterNewsIds = null,
            filterSinceDate = null,
        )
    ): Flow<List<ProtestResource>>
}