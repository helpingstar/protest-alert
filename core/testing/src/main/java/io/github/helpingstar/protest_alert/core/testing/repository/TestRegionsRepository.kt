package io.github.helpingstar.protest_alert.core.testing.repository

import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.data.repository.RegionsRepository
import io.github.helpingstar.protest_alert.core.model.data.Region
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class TestRegionsRepository : RegionsRepository {
    private val regionsFlow: MutableSharedFlow<List<Region>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getRegions(): Flow<List<Region>> = regionsFlow

    override fun getRegion(id: String): Flow<Region> =
        regionsFlow.map { regions -> regions.find { it.id == id }!! }

    fun sendRegions(regions: List<Region>) {
        regionsFlow.tryEmit(regions)
    }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean = true
}