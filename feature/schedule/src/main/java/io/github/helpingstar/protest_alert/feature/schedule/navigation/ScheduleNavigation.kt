package io.github.helpingstar.protest_alert.feature.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.github.helpingstar.protest_alert.feature.schedule.ScheduleScreen
import kotlinx.serialization.Serializable

@Serializable data object ScheduleRoute

fun NavController.navigateToSchedule(navOptions: NavOptions) {
    navigate(route = ScheduleRoute, navOptions)
}

fun NavGraphBuilder.scheduleScreen() {
    composable<ScheduleRoute> {
        ScheduleScreen()
    }
}