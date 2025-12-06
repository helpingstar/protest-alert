package io.github.helpingstar.protest_alert.core.network.supabase

import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabasePaNetwork @Inject constructor(
    private val supabaseClient: SupabaseClient,
) : PaNetworkDataSource {
    override suspend fun getProtestResources(ids: List<Long>?): List<NetworkProtestResource> =
        supabaseClient.from("protests")
            .select {
                if (ids != null) {
                    filter {
                        isIn("id", ids)
                    }
                }
            }
            .decodeList<NetworkProtestResource>()

    override suspend fun getProtestResourceChangeList(after: Int?): List<NetworkChangeList> =
        supabaseClient.from("changelists")
            .select {
                if (after != null) {
                    filter {
                        gt("change_list_version", after)
                    }
                }
            }
            .decodeList<NetworkChangeList>()

}