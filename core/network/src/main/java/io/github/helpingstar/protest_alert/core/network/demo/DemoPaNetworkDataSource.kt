package io.github.helpingstar.protest_alert.core.network.demo

import JvmUnitTestDemoAssetManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import io.github.helpingstar.protest_alert.core.network.Dispatcher
import io.github.helpingstar.protest_alert.core.network.PaDispatchers.IO
import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import io.github.helpingstar.protest_alert.core.network.model.NetworkAnnouncement
import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.BufferedReader
import javax.inject.Inject
import kotlin.time.Instant

class DemoPaNetworkDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: DemoAssetManager = JvmUnitTestDemoAssetManager,
) : PaNetworkDataSource {
    override suspend fun getRegions(ids: List<String>?): List<NetworkRegion> =
        getDataFromJsonFile(REGIONS_ASSET)

    override suspend fun getProtestResources(ids: List<String>?): List<NetworkProtestResource> =
        getDataFromJsonFile(PROTESTS_ASSET)

    override suspend fun getRegionChangeList(after: Instant?): List<NetworkChangeList> =
        getRegions().mapToChangeList(
            idGetter = { it.id.toString() },
            lastUpdatedAtGetter = NetworkRegion::createdAt
        )

    override suspend fun getProtestResourceChangeList(after: Instant?): List<NetworkChangeList> =
        getProtestResources().mapToChangeList(
            idGetter = { it.id.toString() },
            lastUpdatedAtGetter = NetworkProtestResource::updatedAt
        )

    override suspend fun getAnnouncements(ids: List<String>?): List<NetworkAnnouncement> =
        getDataFromJsonFile(ANNOUNCEMENTS_ASSET)

    override suspend fun getAnnouncementChangeList(after: Instant?): List<NetworkChangeList> =
        getAnnouncements().mapToChangeList(
            idGetter = NetworkAnnouncement::id,
            lastUpdatedAtGetter = NetworkAnnouncement::updatedAt
        )

    override suspend fun insertUserFeedback(content: String) = Unit

    @OptIn(ExperimentalSerializationApi::class)
    private suspend inline fun <reified T> getDataFromJsonFile(fileName: String): List<T> =
        withContext(ioDispatcher) {
            assets.open(fileName).use { inputStream ->
                if (SDK_INT <= M) {
                    inputStream.bufferedReader().use(BufferedReader::readText)
                        .let(networkJson::decodeFromString)
                } else {
                    networkJson.decodeFromStream(inputStream)
                }
            }
        }

    companion object {
        private const val REGIONS_ASSET = "regions.json"
        private const val PROTESTS_ASSET = "protests.json"
        private const val ANNOUNCEMENTS_ASSET = "announcements.json"
    }
}

private fun <T> List<T>.mapToChangeList(
    idGetter: (T) -> String,
    lastUpdatedAtGetter: (T) -> Instant,
) = map { item ->
    NetworkChangeList(
        id = idGetter(item),
        lastUpdatedAt = lastUpdatedAtGetter(item),
        isDelete = false
    )
}