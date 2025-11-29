package io.github.helpingstar.protest_alert.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.github.helpingstar.protest_alert.feature.settings.SettingsRoute
import kotlinx.serialization.Serializable

@Serializable data object SettingsRoute

fun NavController.navigateToSetting(navOptions: NavOptions) {
    navigate(route = SettingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable<SettingsRoute> {
        SettingsRoute()
    }
}