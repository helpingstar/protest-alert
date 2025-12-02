package io.github.helpingstar.protest_alert.core.network

import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource

interface PaNetworkDataSource {
    suspend fun getProtestList(): List<NetworkProtestResource>
}