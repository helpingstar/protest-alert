package io.github.helpingstar.protest_alert.feature.settings.impl

import android.Manifest
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.PermissionStatus.Granted
import com.google.accompanist.permissions.rememberPermissionState
import io.github.helpingstar.protest_alert.core.designsystem.component.NavigationListItem
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.core.model.data.Region
import io.github.helpingstar.protest_alert.core.ui.LocalSnackbarHostState
import io.github.helpingstar.protest_alert.core.ui.RegionsTabContent
import io.github.helpingstar.protest_alert.feature.settings.impl.component.FeedbackModal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Instant

private const val TAG = "SettingScreen"

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val feedbackState by viewModel.feedbackState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        feedbackState = feedbackState,
        followRegion = viewModel::followRegion,
        onUpdateNotificationEnabled = viewModel::setUpdateNotificationEnabled,
        onSubmitFeedback = viewModel::submitFeedback,
        onResetFeedbackState = viewModel::resetFeedbackState,
        modifier = modifier
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    feedbackState: FeedbackState,
    followRegion: (String, Boolean) -> Unit,
    onUpdateNotificationEnabled: (Boolean) -> Unit,
    onSubmitFeedback: (String) -> Unit,
    onResetFeedbackState: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val snackbarHostState = LocalSnackbarHostState.current
    val coroutineScope = rememberCoroutineScope()
    val isPreview = LocalInspectionMode.current

    var showFeedbackModal by remember { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf("") }
    val notificationsPermissionState =
        if (!isPreview && VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            rememberPermissionState(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                onPermissionResult = { isGranted ->
                    if (isGranted) {
                        onUpdateNotificationEnabled(true)
                    } else {
                        onUpdateNotificationEnabled(false)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(NOTIFICATION_PERMISSION_DENIED_MESSAGE)
                        }
                    }
                }
            )
        } else {
            null
        }

    // 성공 시 1.5초 후 모달 닫기
    LaunchedEffect(feedbackState) {
        if (feedbackState is FeedbackState.Success) {
            delay(1500L)
            showFeedbackModal = false
            feedbackText = ""
            onResetFeedbackState()
        }
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        when (uiState) {
            SettingsUiState.Loading ->
                Text("Loading")

            is SettingsUiState.Settings ->
                RegionsTabContent(
                    title = "관심 지역 선택",
                    regions = uiState.regions,
                    onFollowButtonClick = followRegion,
                )

            is SettingsUiState.Empty ->
                Text("EMPTY")
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NotificationSwitchListItem(
                checked = (uiState as? SettingsUiState.Settings)?.updateNotificationEnabled == true,
                onCheckedChange = { checked ->
                    if (!checked) {
                        onUpdateNotificationEnabled(false)
                        return@NotificationSwitchListItem
                    }

                    when (val permissionState = notificationsPermissionState) {
                        null -> onUpdateNotificationEnabled(true)
                        else -> when (permissionState.status) {
                            Granted -> onUpdateNotificationEnabled(true)
                            is Denied -> permissionState.launchPermissionRequest()
                        }
                    }
                },
            )

            NavigationListItem(
                "문의 · 건의",
                PaIcons.ChatBubble,
                onClick = { showFeedbackModal = true }
            )

            NavigationListItem(
                "개인정보처리방침",
                PaIcons.Lock,
                onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) }
            )
        }

        if (showFeedbackModal) {
            Dialog(
                onDismissRequest = {
                    showFeedbackModal = false
                    feedbackText = ""
                    onResetFeedbackState()
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false,
                )
            ) {
                FeedbackModal(
                    feedbackText = feedbackText,
                    onFeedbackTextChange = { feedbackText = it },
                    onDismiss = {
                        showFeedbackModal = false
                        feedbackText = ""
                        onResetFeedbackState()
                    },
                    onSend = {
                        onSubmitFeedback(feedbackText)
                    },
                    isSuccess = feedbackState is FeedbackState.Success,
                )
            }
        }

    }
}

@Composable
private fun NotificationSwitchListItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        color = PaColor.backgroundSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, PaColor.borderDefault),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = PaIcons.NotificationsBorder),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = PaColor.accentPrimary,
                    )
                }

                Text(
                    text = "업데이트 알림",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PaColor.textPrimary,
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = PaColor.backgroundSurface,
                    checkedTrackColor = PaColor.accentPrimary,
                    checkedBorderColor = PaColor.accentPrimary,
                ),
            )
        }
    }
}

@Preview(
    widthDp = 390,
    heightDp = 844,
    showBackground = true
)
@Composable
private fun SettingsScreenPreview() {
    val sampleRegions = listOf(
        FollowableRegion(Region("서울", "서울", Instant.DISTANT_PAST), isFollowed = true),
        FollowableRegion(Region("경기", "경기북부", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("인천", "인천", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("부산", "부산", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("대구", "대구", Instant.DISTANT_PAST), isFollowed = false),
        FollowableRegion(Region("대전", "대전", Instant.DISTANT_PAST), isFollowed = false),
    )
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        PaTheme {
            SettingsScreen(
                uiState = SettingsUiState.Settings(
                    regions = sampleRegions,
                    updateNotificationEnabled = true,
                ),
                feedbackState = FeedbackState.Idle,
                followRegion = { _, _ -> },
                onUpdateNotificationEnabled = { },
                onSubmitFeedback = { },
                onResetFeedbackState = { },
            )
        }
    }
}

private const val PRIVACY_POLICY_URL = "https://helpingstar.github.io/app/protestalert/privacy/"
private const val NOTIFICATION_PERMISSION_DENIED_MESSAGE = "알림을 받으려면 알림 권한이 필요합니다."
