package io.github.helpingstar.protest_alert.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.feature.schedule.api.ScheduleNavKey
import io.github.helpingstar.protest_alert.feature.settings.api.navigation.SettingsNavKey
import io.github.helpingstar.protest_alert.feature.schedule.R as scheduleR
import io.github.helpingstar.protest_alert.feature.settings.R as settingsR

data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)

val SCHEDULE = TopLevelNavItem(
    selectedIcon = PaIcons.Calendar,
    unselectedIcon = PaIcons.CalendarBorder,
    iconTextId = scheduleR.string.feature_schedule_nav,
    titleTextId = scheduleR.string.feature_schedule_title,
)

val SETTINGS = TopLevelNavItem(
    selectedIcon = PaIcons.Settings,
    unselectedIcon = PaIcons.SettingsBorder,
    iconTextId = settingsR.string.feature_setting_title,
    titleTextId = settingsR.string.feature_setting_title,
)


val TOP_LEVEL_NAV_ITEMS = mapOf(
    ScheduleNavKey to SCHEDULE,
    SettingsNavKey to SETTINGS,
)