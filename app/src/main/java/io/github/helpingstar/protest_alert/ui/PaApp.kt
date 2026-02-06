package io.github.helpingstar.protest_alert.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.github.helpingstar.protest_alert.MainActivityViewModel
import io.github.helpingstar.protest_alert.R
import io.github.helpingstar.protest_alert.core.designsystem.component.PaBackground
import io.github.helpingstar.protest_alert.core.designsystem.component.PaNavigationSuiteScaffold
import io.github.helpingstar.protest_alert.core.designsystem.component.PaTopAppbar
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.navigation.Navigator
import io.github.helpingstar.protest_alert.core.ui.LocalSnackbarHostState
import io.github.helpingstar.protest_alert.feature.schedule.impl.navigation.scheduleEntry
import io.github.helpingstar.protest_alert.feature.settings.impl.navigation.settingsEntry
import io.github.helpingstar.protest_alert.navigation.TOP_LEVEL_NAV_ITEMS
import io.github.helpingstar.protest_alert.ui.component.AnnouncementBottomSheet

private const val TAG = "PaApp"

@Composable
fun PaApp(
    appState: PaAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    PaBackground(modifier = modifier) {
        val snackbarHostState = remember { SnackbarHostState() }

        val isOffline by appState.isOffline.collectAsStateWithLifecycle()
        Log.d(TAG, "isOffline: $isOffline")

        val notConnectedMessage = stringResource(R.string.not_connected)
        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showSnackbar(
                    message = notConnectedMessage,
                    duration = Indefinite
                )
            }
        }
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            PaAppContent(
                appState = appState,
                windowAdaptiveInfo = windowAdaptiveInfo
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PaAppContent(
    appState: PaAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    viewModel: MainActivityViewModel = hiltViewModel(),
) {
    val snackbarHostState = LocalSnackbarHostState.current

    val navigator = remember { Navigator(appState.navigationState) }

    // Announcement state
    val showAnnouncementSheet by viewModel.showAnnouncementSheet.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val hasUnreadAnnouncements by viewModel.hasUnreadAnnouncements.collectAsStateWithLifecycle()

    PaNavigationSuiteScaffold(
        navigationSuiteItems = {
            TOP_LEVEL_NAV_ITEMS.forEach { (navKey, navItem) ->
                val selected = navKey == appState.navigationState.currentTopLevelKey
                item(
                    selected = selected,
                    onClick = { navigator.navigate(navKey) },
                    icon = {
                        Icon(
                            imageVector = navItem.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = navItem.selectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(navItem.iconTextId)) },
                    modifier = Modifier
                )
            }
        },
        windowAdaptiveInfo = windowAdaptiveInfo
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(
                    snackbarHostState,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.exclude(
                            WindowInsets.ime,
                        )
                    )
                )
            }
        ) { padding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                var shouldShowTopAppBar = false

                if (appState.navigationState.currentTopLevelKey in appState.navigationState.topLevelKeys) {
                    shouldShowTopAppBar = true

                    val destination =
                        TOP_LEVEL_NAV_ITEMS[appState.navigationState.currentTopLevelKey]
                            ?: error("Top level nav item not found for ${appState.navigationState.currentTopLevelKey}")

                    PaTopAppbar(
                        titleRes = destination.titleTextId,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        ),
                        actionIconRes = if (hasUnreadAnnouncements) {
                            PaIcons.NotificationsUnreadBorder
                        } else {
                            PaIcons.NotificationsBorder
                        },
                        onActionClick = { viewModel.openAnnouncementSheet() }
                    )
                }

                Box(
                    modifier = Modifier.consumeWindowInsets(
                        if (shouldShowTopAppBar) {
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                        } else {
                            WindowInsets(0, 0, 0, 0)
                        }
                    )
                ) {
                    val entryProvider = entryProvider {
                        scheduleEntry(navigator)
                        settingsEntry(navigator)
                    }

                    NavDisplay(
                        backStack = appState.navigationState.topLevelStack,
                        onBack = { navigator.goBack() },
                        modifier = modifier,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                        entryProvider = entryProvider
                    )
                }
            }
        }

        // Announcement BottomSheet
        if (showAnnouncementSheet) {
            AnnouncementBottomSheet(
                announcements = announcements,
                onDismiss = { viewModel.closeAnnouncementSheet() },
                onMarkAllRead = { viewModel.markAllAsRead() },
                onAnnouncementClick = { id -> viewModel.markAsRead(id) }
            )
        }
    }
}