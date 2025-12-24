package io.github.helpingstar.protest_alert.feature.settings.impl.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.github.helpingstar.protest_alert.feature.settings.impl.SettingsRoute
import kotlinx.serialization.Serializable

@Serializable
data class SettingsRoute(
    val initialRegionId: String? = null,
)

fun NavController.navigateToSetting(
    initialRegionId: String? = null,
    navOptions: NavOptions? = null,
) {
    navigate(route = SettingsRoute(initialRegionId), navOptions)
}

fun NavGraphBuilder.settingsScreen() {
    composable<SettingsRoute> {
        SettingsRoute()
    }
}