package io.github.helpingstar.protest_alert.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.github.helpingstar.protest_alert.feature.schedule.navigation.ScheduleRoute
import io.github.helpingstar.protest_alert.feature.schedule.navigation.scheduleScreen
import io.github.helpingstar.protest_alert.feature.settings.navigation.settingsScreen
import io.github.helpingstar.protest_alert.ui.PaAppState

@Composable
fun PaNavHost(
    appState: PaAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = ScheduleRoute,
        modifier = modifier,
    ) {
        scheduleScreen()

        settingsScreen()
    }
}