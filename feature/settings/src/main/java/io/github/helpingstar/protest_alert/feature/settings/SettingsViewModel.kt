package io.github.helpingstar.protest_alert.feature.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.helpingstar.protest_alert.core.data.repository.UserDataRepository
import io.github.helpingstar.protest_alert.core.domain.GetFollowableRegionsUseCase
import io.github.helpingstar.protest_alert.core.domain.RegionSortField
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.feature.settings.navigation.SettingsRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    getFollowableRegions: GetFollowableRegionsUseCase,
) : ViewModel() {
    private val selectedRegionIdKey = "selectedRegionIdKey"

    private val settingsRoute: SettingsRoute = savedStateHandle.toRoute()
    private val selectedRegionId = savedStateHandle.getStateFlow(
        key = selectedRegionIdKey,
        initialValue = settingsRoute.initialRegionId,
    )

    val uiState: StateFlow<SettingsUiState> = combine(
        selectedRegionId,
        getFollowableRegions(RegionSortField.NAME),
        SettingsUiState::Settings,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState.Loading
    )

    fun followRegion(followedRegionId: Long, followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.setRegionIdFollowed(followedRegionId, followed)
        }
    }

    fun onRegionClick(regionId: Long) {
        savedStateHandle[selectedRegionIdKey] = regionId
    }

}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Settings(
        val selectedRegionId: String?,
        val regions: List<FollowableRegion>,
    ) : SettingsUiState

    data object Empty : SettingsUiState
}