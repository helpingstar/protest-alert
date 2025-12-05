package io.github.helpingstar.protest_alert.sync.status

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkInfo.State
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.helpingstar.protest_alert.core.data.util.SyncManager
import io.github.helpingstar.protest_alert.sync.initializers.SYNC_WORK_NAME
import io.github.helpingstar.protest_alert.sync.workers.SyncWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : SyncManager {
    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
            .map(List<WorkInfo>::anyRunning)
            .conflate()

    override fun requestSync() {
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            SyncWorker.startUpSyncWork()
        )
    }
}

private fun List<WorkInfo>.anyRunning() = any { it.state == State.RUNNING }