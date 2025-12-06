package io.github.helpingstar.protest_alert.core.network

import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource

interface PaNetworkDataSource {
    suspend fun getProtestResources(ids: List<Long>? = null): List<NetworkProtestResource>

    suspend fun getProtestResourceChangeList(after: Int? = null): List<NetworkChangeList>
}