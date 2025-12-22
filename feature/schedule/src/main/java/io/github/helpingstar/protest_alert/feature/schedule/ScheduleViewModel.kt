package io.github.helpingstar.protest_alert.feature.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.helpingstar.protest_alert.core.data.repository.UserDataRepository
import io.github.helpingstar.protest_alert.core.data.repository.UserProtestResourceRepository
import io.github.helpingstar.protest_alert.core.data.util.SyncManager
import io.github.helpingstar.protest_alert.core.domain.GetFollowableRegionsUseCase
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    syncManager: SyncManager,
    private val userDataRepository: UserDataRepository,
    userProtestResourceRepository: UserProtestResourceRepository,
    getFollowableRegions: GetFollowableRegionsUseCase,
) : ViewModel() {
    private val shouldShowOnboarding: Flow<Boolean> =
        userDataRepository.userData.map { !it.shouldHideOnboarding }

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

    val onboardingUiState: StateFlow<OnboardingUiState> =
        combine(
            shouldShowOnboarding,
            getFollowableRegions(),
        ) { shouldShowOnboarding, regions ->
            if (shouldShowOnboarding) {
                OnboardingUiState.Shown(regions = regions)
            } else {
                OnboardingUiState.NotShown
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = OnboardingUiState.Loading
            )

    fun dismissOnboarding() {
        viewModelScope.launch {
            userDataRepository.setShouldHideOnboarding(true)
        }
    }
}
