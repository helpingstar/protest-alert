package io.github.helpingstar.protest_alert.core.ui

import io.github.helpingstar.protest_alert.core.model.data.ProtestResource

sealed interface ProtestFeedUiState {
    data object Loading : ProtestFeedUiState

    data class Success(
        val feed: List<ProtestResource>
    ) : ProtestFeedUiState
}