package io.github.helpingstar.protest_alert.feature.schedule.impl

import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion

sealed interface OnboardingUiState {
    data object Loading : OnboardingUiState
    data object LoadFailed : OnboardingUiState
    data object NotShown : OnboardingUiState
    data class Shown(
        val regions: List<FollowableRegion>,
    ) : OnboardingUiState {
        val isDismissable: Boolean get() = regions.any { it.isFollowed }
    }
}