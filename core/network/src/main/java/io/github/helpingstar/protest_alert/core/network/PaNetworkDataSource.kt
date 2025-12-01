package io.github.helpingstar.protest_alert.core.network

import io.github.helpingstar.protest_alert.core.network.data.Protest

interface PaNetworkDataSource {
    suspend fun getProtestList(): List<Protest>
}