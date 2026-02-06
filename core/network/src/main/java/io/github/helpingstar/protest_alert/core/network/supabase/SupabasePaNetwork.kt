package io.github.helpingstar.protest_alert.core.network.supabase

import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkAnnouncement
import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import io.github.helpingstar.protest_alert.core.network.model.NetworkUserFeedback
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Instant

@Singleton

class SupabasePaNetwork @Inject constructor(
    private val supabaseClient: SupabaseClient,
) : PaNetworkDataSource {

    override suspend fun getRegions(ids: List<String>?): List<NetworkRegion> =
        supabaseClient.from("regions")
            .select {
                if (ids != null) {
                    filter {
                        isIn("id", ids)
                    }
                }
            }
            .decodeList<NetworkRegion>()

    override suspend fun getProtestResources(ids: List<String>?): List<NetworkProtestResource> =
        supabaseClient.from("protests")
            .select {
                if (ids != null) {
                    filter {
                        isIn("id", ids)
                    }
                }
            }
            .decodeList<NetworkProtestResource>()

    override suspend fun getRegionChangeList(after: Instant?): List<NetworkChangeList> =
        supabaseClient.from("regions")
            .select {
                if (after != null) {
                    filter {
                        gt("created_at", after.toString())
                    }
                }
            }
            .decodeList<NetworkRegion>()
            .map { region ->
                NetworkChangeList(
                    id = region.id.toString(),
                    lastUpdatedAt = region.createdAt,
                    isDelete = false
                )
            }

    override suspend fun getProtestResourceChangeList(after: Instant?): List<NetworkChangeList> =
        supabaseClient.from("protests")
            .select {
                if (after != null) {
                    filter {
                        gt("updated_at", after.toString())
                    }
                }
            }
            .decodeList<NetworkProtestResource>()
            .map { protestResource ->
                NetworkChangeList(
                    id = protestResource.id.toString(),
                    lastUpdatedAt = protestResource.updatedAt,
                    isDelete = false
                )
            }

    override suspend fun getAnnouncements(ids: List<String>?): List<NetworkAnnouncement> =
        supabaseClient.from("announcements")
            .select {
                filter {
                    eq("status", "published")
                    lte(
                        "start_at",
                        Instant.fromEpochMilliseconds(System.currentTimeMillis()).toString()
                    )
                    or {
                        exact("end_at", null)
                        gte(
                            "end_at",
                            Instant.fromEpochMilliseconds(System.currentTimeMillis()).toString()
                        )
                    }
                    if (ids != null) {
                        isIn("id", ids)
                    }
                }
            }
            .decodeList<NetworkAnnouncement>()

    override suspend fun getAnnouncementChangeList(after: Instant?): List<NetworkChangeList> =
        supabaseClient.from("announcements")
            .select {
                filter {
                    isIn("status", listOf("published", "archived"))
                    if (after != null) {
                        gt("updated_at", after.toString())
                    }
                }
            }
            .decodeList<NetworkAnnouncement>()
            .map { announcement ->
                NetworkChangeList(
                    id = announcement.id,
                    lastUpdatedAt = announcement.updatedAt,
                    isDelete = announcement.status == "archived"
                )
            }

    override suspend fun insertUserFeedback(content: String) {
        supabaseClient.from("user_feedbacks")
            .insert(NetworkUserFeedback(content = content))
    }

}