package io.github.helpingstar.protest_alert.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.data.repository.ProtestRepository
import io.github.helpingstar.protest_alert.core.datastore.ChangeListVersions
import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource
import io.github.helpingstar.protest_alert.core.network.Dispatcher
import io.github.helpingstar.protest_alert.core.network.NiaDispatchers.IO
import io.github.helpingstar.protest_alert.sync.initializers.SyncConstraints
import io.github.helpingstar.protest_alert.sync.initializers.syncForegroundInfo
import io.github.helpingstar.protest_alert.sync.status.SyncSubscriber
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val paPreferences: PaPreferencesDataSource,
    private val protestRepository: ProtestRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val syncSubscriber: SyncSubscriber,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        syncSubscriber.subscribe()

        val syncedSuccessfully = awaitAll(
            async { protestRepository.sync() }
        ).all { it }

        if (syncedSuccessfully) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    override suspend fun getChangeListVersions(): ChangeListVersions =
        paPreferences.getChangeListVersions()

    override suspend fun updateChangeListVersions(
        update: ChangeListVersions.() -> ChangeListVersions,
    ) = paPreferences.updateChangeListVersion(update)

    companion object {
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }

}