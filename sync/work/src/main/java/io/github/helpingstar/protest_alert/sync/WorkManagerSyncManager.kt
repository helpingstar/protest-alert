package io.github.helpingstar.protest_alert.sync

import android.content.Context
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.helpingstar.protest_alert.core.data.util.SyncManager
import kotlinx.coroutines.flow.Flow

internal class WorkManagerSyncManager(
    @ApplicationContext private val context: Context,
) : SyncManager {
    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(SYNC_WORK)


    override fun requestSync() {
        TODO("Not yet implemented")
    }

}