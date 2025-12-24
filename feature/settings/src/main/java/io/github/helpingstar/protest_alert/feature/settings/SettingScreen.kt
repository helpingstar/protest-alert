package io.github.helpingstar.protest_alert.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.ui.RegionsTabContent

private const val TAG = "SettingScreen"

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        followRegion = viewModel::followRegion,
        modifier = modifier
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    followRegion: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            SettingsUiState.Loading ->
                Text("Loading")

            is SettingsUiState.Settings ->
                RegionsTabContent(
                    regions = uiState.regions,
                    onFollowButtonClick = followRegion,
                )

            is SettingsUiState.Empty ->
                Text("EMPTY")
        }
    }
}