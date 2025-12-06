package io.github.helpingstar.protest_alert.feature.schedule

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState

private val CardBackgroundColor = Color(0xFFF3F4F6)
private val TextPrimaryColor = Color(0xFF171A1F)
private val TextSecondaryColor = Color(0xFF565D6D)
private val TagBackgroundColor = Color(0x1A3899FA)
private val TagTextColor = Color(0xFF3899FA)

//sealed interface ProtestUiState {
//    data object Loading: ProtestUiState
//    data class Success(
//        val protest: List<>
//    )
//}

@Composable
internal fun ScheduleRoute(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()

    ScheduleScreen(
        feedState = feedState,
        modifier = modifier
    )
}

@Composable
internal fun ScheduleScreen(
    feedState: ProtestFeedUiState,
    modifier: Modifier = Modifier
) {
    val isFeedLoading = feedState is ProtestFeedUiState.Loading

    when (feedState) {
        ProtestFeedUiState.Loading -> Unit
        is ProtestFeedUiState.Success -> {
            Text("Hello")
        }
    }
}
