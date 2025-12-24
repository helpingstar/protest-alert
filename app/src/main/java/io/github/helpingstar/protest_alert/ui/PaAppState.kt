package io.github.helpingstar.protest_alert.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.helpingstar.protest_alert.core.data.util.NetworkMonitor
import io.github.helpingstar.protest_alert.core.navigation.NavigationState
import io.github.helpingstar.protest_alert.core.navigation.rememberNavigationState
import io.github.helpingstar.protest_alert.feature.schedule.api.ScheduleNavKey
import io.github.helpingstar.protest_alert.navigation.TOP_LEVEL_NAV_ITEMS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val TAG = "PaAppState"

@Composable
fun rememberPaAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): PaAppState {
    val navigationState = rememberNavigationState(ScheduleNavKey, TOP_LEVEL_NAV_ITEMS.keys)

    return remember(
        navigationState,
        coroutineScope,
        networkMonitor,
    ) {
        PaAppState(
            navigationState = navigationState,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }
}

@Stable
class PaAppState(
    val navigationState: NavigationState,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
}