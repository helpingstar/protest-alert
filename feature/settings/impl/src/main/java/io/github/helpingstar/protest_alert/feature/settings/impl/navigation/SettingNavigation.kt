package io.github.helpingstar.protest_alert.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.helpingstar.protest_alert.core.navigation.Navigator
import io.github.helpingstar.protest_alert.feature.settings.api.navigation.SettingsNavKey
import io.github.helpingstar.protest_alert.feature.settings.impl.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry(navigator: Navigator) {
    entry<SettingsNavKey> {
        SettingsScreen()
    }
}