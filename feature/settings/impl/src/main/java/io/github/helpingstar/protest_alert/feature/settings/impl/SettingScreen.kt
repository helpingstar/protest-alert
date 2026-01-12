package io.github.helpingstar.protest_alert.feature.settings.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.designsystem.component.NavigationListItem
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.core.model.data.Region
import io.github.helpingstar.protest_alert.core.ui.RegionsTabContent
import kotlin.time.Instant

private const val TAG = "SettingScreen"

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        followRegion = viewModel::followRegion,
        modifier = modifier
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    followRegion: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier.padding(16.dp),
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
            NavigationListItem(
                "개인정보처리방침",
                PaIcons.Lock,
                onClick = { uriHandler.openUri(PRIVACY_POLICY_URL) }
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

    SettingsScreen(
        uiState = SettingsUiState.Settings(regions = sampleRegions),
        followRegion = { _, _ -> },
    )
}

private const val PRIVACY_POLICY_URL = "https://helpingstar.github.io/app/protestalert/privacy/"