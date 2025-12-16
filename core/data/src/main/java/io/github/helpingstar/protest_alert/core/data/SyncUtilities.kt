package io.github.helpingstar.protest_alert.core.data

import android.util.Log
import io.github.helpingstar.protest_alert.core.datastore.LastUpdatedAt
import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private const val TAG = "SyncUtilities"

interface Synchronizer {
    suspend fun getLastUpdatedAt(): LastUpdatedAt

    suspend fun updateChangeListVersions(update: LastUpdatedAt.() -> LastUpdatedAt)

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}

interface Syncable {
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.i(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception,
    )
    Result.failure(exception)
}

@OptIn(ExperimentalTime::class)
suspend fun Synchronizer.changeListSync(
    versionReader: (LastUpdatedAt) -> Instant,
    changeListFetcher: suspend (Instant) -> List<NetworkChangeList>,
    versionUpdater: LastUpdatedAt.(Instant) -> LastUpdatedAt,
    modelDeleter: suspend (List<String>) -> Unit,
    modelUpdater: suspend (List<String>) -> Unit,
) = suspendRunCatching {
    // Fetch the change list since last sync (akin to a git fetch)
    val currentVersion = versionReader(getLastUpdatedAt())
    val changeList = changeListFetcher(currentVersion)
    Log.d(TAG, changeList.toString())
    if (changeList.isEmpty()) return@suspendRunCatching true

    val (deleted, updated) = changeList.partition(NetworkChangeList::isDelete)

    // Delete models that have been deleted server-side
    modelDeleter(deleted.map(NetworkChangeList::id))

    // Using the change list, pull down and save the changes (akin to a git pull)
    modelUpdater(updated.map(NetworkChangeList::id))

    // Update the last synced version (akin to updating local git HEAD)
    val latestVersion = changeList.last().lastUpdatedAt
    updateChangeListVersions {
        versionUpdater(latestVersion)
    }
}.isSuccess