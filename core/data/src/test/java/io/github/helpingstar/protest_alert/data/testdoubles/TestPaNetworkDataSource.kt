package io.github.helpingstar.protest_alert.data.testdoubles

import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.demo.DemoPaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkAnnouncement
import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.json.Json
import kotlin.time.Instant

enum class CollectionType {
    Regions,
    ProtestResources,
    Announcements,
}

class TestPaNetworkDataSource : PaNetworkDataSource {
    private val source = DemoPaNetworkDataSource(
        UnconfinedTestDispatcher(),
        Json { ignoreUnknownKeys = true },
    )

    private val allRegions = runBlocking { source.getRegions() }

    private val allProtestResource = runBlocking { source.getProtestResources() }

    private val allAnnouncements = runBlocking { source.getAnnouncements() }

    private val changeLists: MutableMap<CollectionType, List<NetworkChangeList>> = mutableMapOf(
        CollectionType.Regions to allRegions
            .mapToChangeList(
                idGetter = { it.id.toString() },
                lastUpdatedAtGetter = NetworkRegion::createdAt,
            ),
        CollectionType.ProtestResources to allProtestResource
            .mapToChangeList(
                idGetter = { it.id.toString() },
                lastUpdatedAtGetter = NetworkProtestResource::updatedAt,
            ),
        CollectionType.Announcements to allAnnouncements
            .mapToChangeList(
                idGetter = NetworkAnnouncement::id,
                lastUpdatedAtGetter = NetworkAnnouncement::updatedAt
            )
    )

    override suspend fun getRegions(ids: List<String>?): List<NetworkRegion> =
        allRegions.matchIds(
            ids = ids,
            idGetter = { it.id.toString() },
        )

    override suspend fun getProtestResources(ids: List<String>?): List<NetworkProtestResource> =
        allProtestResource.matchIds(
            ids = ids,
            idGetter = { it.id.toString() },
        )

    override suspend fun getRegionChangeList(after: Instant?): List<NetworkChangeList> =
        changeLists.getValue(CollectionType.Regions).after(after)

    override suspend fun getProtestResourceChangeList(after: Instant?): List<NetworkChangeList> =
        changeLists.getValue(CollectionType.ProtestResources).after(after)

    override suspend fun getAnnouncements(ids: List<String>?): List<NetworkAnnouncement> =
        allAnnouncements.matchIds(
            ids = ids,
            idGetter = NetworkAnnouncement::id,
        )

    override suspend fun getAnnouncementChangeList(after: Instant?): List<NetworkChangeList> =
        changeLists.getValue(CollectionType.Announcements).after(after)

    override suspend fun insertUserFeedback(content: String) = Unit

    fun latestChangeListVersion(collectionType: CollectionType) =
        changeLists.getValue(collectionType).maxOf(NetworkChangeList::lastUpdatedAt)

    fun changeListsAfter(
        collectionType: CollectionType,
        lastUpdatedAt: Instant,
    ): List<NetworkChangeList> =
        changeLists.getValue(collectionType).after(lastUpdatedAt)

    fun editCollection(
        collectionType: CollectionType,
        id: String,
        lastUpdatedAt: Instant,
        isDelete: Boolean,
    ) {
        val changeList = changeLists.getValue(collectionType)
        val change = NetworkChangeList(
            id = id,
            lastUpdatedAt = lastUpdatedAt,
            isDelete = isDelete,
        )
        changeLists[collectionType] = changeList.filterNot { it.id == id } + change
    }
}

fun List<NetworkChangeList>.after(lastUpdatedAt: Instant?): List<NetworkChangeList> =
    when (lastUpdatedAt) {
        null -> this
        else -> filter { it.lastUpdatedAt > lastUpdatedAt }
    }

private fun <T> List<T>.matchIds(
    ids: List<String>?,
    idGetter: (T) -> String,
) = when (ids) {
    null -> this
    else -> ids.toSet().let { idSet -> filter { idGetter(it) in idSet } }
}

private fun <T> List<T>.mapToChangeList(
    idGetter: (T) -> String,
    lastUpdatedAtGetter: (T) -> Instant,
) = map { item ->
    NetworkChangeList(
        id = idGetter(item),
        lastUpdatedAt = lastUpdatedAtGetter(item),
        isDelete = false,
    )
}