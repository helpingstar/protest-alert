package io.github.helpingstar.protest_alert.feature.settings.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.helpingstar.protest_alert.core.data.repository.UserDataRepository
import io.github.helpingstar.protest_alert.core.domain.GetFollowableRegionsUseCase
import io.github.helpingstar.protest_alert.core.domain.RegionSortField
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val userDataRepository: UserDataRepository,
    getFollowableRegions: GetFollowableRegionsUseCase,
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = getFollowableRegions(RegionSortField.NAME)
        .map { regions -> SettingsUiState.Settings(regions) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState.Loading
        )

    fun followRegion(followedRegionId: String, followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.setRegionIdFollowed(followedRegionId, followed)
        }
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Settings(
        val regions: List<FollowableRegion>,
    ) : SettingsUiState

    data object Empty : SettingsUiState
}