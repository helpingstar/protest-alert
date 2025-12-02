package io.github.helpingstar.protest_alert.core.network.supabase

import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabasePaNetwork @Inject constructor(
    private val supabaseClient: SupabaseClient,
) : PaNetworkDataSource {
    override suspend fun getProtestList(): List<NetworkProtestResource> =
        supabaseClient.from("protests")
            .select()
            .decodeList<NetworkProtestResource>()

}