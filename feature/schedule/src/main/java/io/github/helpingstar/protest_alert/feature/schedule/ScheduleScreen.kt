package io.github.helpingstar.protest_alert.feature.schedule

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ScheduleRoute(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    ScheduleScreen()
}

@Composable
internal fun ScheduleScreen(
    modifier: Modifier = Modifier
) {
    Text("SCHEDULE")
}