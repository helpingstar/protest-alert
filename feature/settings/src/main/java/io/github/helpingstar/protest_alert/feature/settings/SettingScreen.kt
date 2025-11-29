package io.github.helpingstar.protest_alert.feature.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    SettingsScreen()
}

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    Text("SETTINGS")
}