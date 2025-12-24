package io.github.helpingstar.protest_alert.feature.schedule.impl.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.github.helpingstar.protest_alert.feature.schedule.impl.ScheduleScreen
import kotlinx.serialization.Serializable

@Serializable
data object ScheduleRoute

fun NavController.navigateToSchedule(navOptions: NavOptions) {
    navigate(route = ScheduleRoute, navOptions)
}

fun NavGraphBuilder.scheduleScreen() {
    composable<ScheduleRoute> {
        ScheduleScreen()
    }
}