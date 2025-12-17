package io.github.helpingstar.protest_alert.feature.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.helpingstar.protest_alert.core.data.repository.UserDataRepository
import io.github.helpingstar.protest_alert.core.data.repository.UserProtestResourceRepository
import io.github.helpingstar.protest_alert.core.data.util.SyncManager
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    syncManager: SyncManager,
    private val userDataRepository: UserDataRepository,
    userProtestResourceRepository: UserProtestResourceRepository,
) : ViewModel() {

    val isSyncing = syncManager.isSyncing
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val feedState: StateFlow<ProtestFeedUiState> =
        userProtestResourceRepository.observeAllForFollowedRegions()
            .map(ProtestFeedUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ProtestFeedUiState.Loading
            )
}
