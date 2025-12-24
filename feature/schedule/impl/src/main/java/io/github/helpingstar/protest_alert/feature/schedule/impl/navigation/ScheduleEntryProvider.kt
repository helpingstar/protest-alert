package io.github.helpingstar.protest_alert.feature.schedule.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.helpingstar.protest_alert.core.navigation.Navigator
import io.github.helpingstar.protest_alert.feature.schedule.api.ScheduleNavKey
import io.github.helpingstar.protest_alert.feature.schedule.impl.ScheduleScreen

fun EntryProviderScope<NavKey>.scheduleEntry(navigator: Navigator) {
    entry<ScheduleNavKey> {
        ScheduleScreen()
    }
}