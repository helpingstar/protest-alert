package io.github.helpingstar.protest_alert.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.feature.schedule.impl.navigation.ScheduleRoute
import io.github.helpingstar.protest_alert.feature.settings.impl.navigation.SettingsRoute
import kotlin.reflect.KClass
import io.github.helpingstar.protest_alert.feature.schedule.R as scheduleR
import io.github.helpingstar.protest_alert.feature.settings.R as settingsR

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    SCHEDULE(
        selectedIcon = PaIcons.Calendar,
        unselectedIcon = PaIcons.CalendarBorder,
        iconTextId = scheduleR.string.feature_schedule_nav,
        titleTextId = scheduleR.string.feature_schedule_title,
        route = ScheduleRoute::class,
    ),
    SETTINGS(
        selectedIcon = PaIcons.Settings,
        unselectedIcon = PaIcons.SettingsBorder,
        iconTextId = settingsR.string.feature_setting_title,
        titleTextId = settingsR.string.feature_setting_title,
        route = SettingsRoute::class
    )
}