package io.github.helpingstar.protest_alert.core.network

import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface PaNetworkDataSource {
    suspend fun getRegions(ids: List<String>? = null): List<NetworkRegion>

    suspend fun getProtestResources(ids: List<String>? = null): List<NetworkProtestResource>

    suspend fun getRegionChangeList(after: Instant? = null): List<NetworkChangeList>

    suspend fun getProtestResourceChangeList(after: Instant? = null): List<NetworkChangeList>
}
