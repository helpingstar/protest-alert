package io.github.helpingstar.protest_alert.feature.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily
import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState
import io.github.helpingstar.protest_alert.core.ui.RegionsTabContent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.ExperimentalTime


// Colors from Figma design
private val TextPrimary = Color(0xFF171A1F)
private val TextSecondary = Color(0xFF787878)
private val TagBackground = Color(0x1A3899FA) // rgba(56,153,250,0.1)
private val TagText = Color(0xFF3899FA)
private val CardBackground = Color(0x99F3F4F6) // rgba(243,244,246,0.6)

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val onboardingUiState by viewModel.onboardingUiState.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    ScheduleScreen(
        isSyncing = isSyncing,
        onboardingUiState = onboardingUiState,
        feedState = feedState,
        onRegionCheckedChanged = viewModel::updateRegionSelection,
        saveFollowedRegions = viewModel::dismissOnboarding,
        modifier = modifier
    )
}

@Composable
internal fun ScheduleScreen(
    isSyncing: Boolean,
    onboardingUiState: OnboardingUiState,
    onRegionCheckedChanged: (String, Boolean) -> Unit,
    feedState: ProtestFeedUiState,
    saveFollowedRegions: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isOnboardingLoading = onboardingUiState is OnboardingUiState.Loading
    val isFeedLoading = feedState is ProtestFeedUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column() {
            Onboarding(
                onboardingUiState = onboardingUiState,
                onRegionCheckedChanged = onRegionCheckedChanged,
                saveFollowedRegions = saveFollowedRegions
            )

            ScheduleContent(
                feedState = feedState,
                modifier = modifier
            )
        }

    }
}

@Composable
private fun Onboarding(
    onboardingUiState: OnboardingUiState,
    onRegionCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedRegions: () -> Unit,
) {
    when (onboardingUiState) {
        OnboardingUiState.Loading,
        OnboardingUiState.LoadFailed,
        OnboardingUiState.NotShown,
            -> Unit

        is OnboardingUiState.Shown -> {
            RegionsTabContent(
                regions = onboardingUiState.regions,
                onFollowButtonClick = onRegionCheckedChanged,
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = saveFollowedRegions,
                    enabled = onboardingUiState.isDismissable,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .widthIn(364.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "완료",
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalTime::class)
@Composable
private fun ScheduleContent(
    feedState: ProtestFeedUiState,
    modifier: Modifier = Modifier
) {

    when (feedState) {
        ProtestFeedUiState.Loading -> Unit
        is ProtestFeedUiState.Success -> {
            val groupedProtests = groupProtestsByDate(feedState.feed)

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 17.dp),
            ) {
                groupedProtests.forEach { (date, protestsForDate) ->
                    item(key = "header_$date") {
                        DateHeader(date = date)
                    }

                    itemsIndexed(
                        items = protestsForDate,
                        key = { _, protest -> protest.id }
                    ) { index, protest ->
                        ScheduleItem(protest = protest)
                        if (index < protestsForDate.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(17.dp))
                    }
                }
            }
        }
    }

}

@Composable
private fun DateHeader(
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    val formattedDate = "${date.year}년 ${String.format(Locale.ROOT, "%02d", date.month.number)}월 ${
        String.format(
            Locale.ROOT, "%02d",
            date.day
        )
    }일"

    Text(
        text = formattedDate,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamily,
        color = TextPrimary,
        lineHeight = 28.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 11.dp)
    )
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ScheduleItem(
    protest: UserProtestResource,
    modifier: Modifier = Modifier
) {
    val (startTime, endTime) = formatTimeRangePair(protest)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Time text - vertically centered, 3 lines (start, ~, end)
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$startTime\n~\n$endTime",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily,
                    color = TextPrimary,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 25.dp)
                    .weight(1f)
            ) {
                // Location text - top
                Text(
                    text = protest.location ?: "-",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    fontFamily = fontFamily,
                    lineHeight = 21.sp,
                    modifier = Modifier
                )

                // Bottom row: participants info
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = TextSecondary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "참여자 ${formatParticipantsWithComma(protest.participants)}명",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }


            // Region tag - vertically centered, right side
            RegionTag(
                region = protest.region,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun RegionTag(
    region: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(22.dp)
            .background(
                color = TagBackground,
                shape = RoundedCornerShape(11.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = region,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            color = TagText,
            lineHeight = 20.sp
        )
    }
}

@OptIn(ExperimentalTime::class)
private fun formatTimeRangePair(protest: UserProtestResource): Pair<String, String> {
    val timeZone = TimeZone.of("Asia/Seoul")

    val startTime = protest.startAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    val endTime = protest.endAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    return Pair(startTime, endTime)
}

private fun formatParticipantsWithComma(count: Int?): String {
    return count?.let {
        String.format(Locale.ROOT, "%,d", it)
    } ?: "0"
}

/**
 * Groups protests by date, sorted in descending order (most recent first).
 * Within the same date, protests are sorted by start time in ascending order.
 */
@OptIn(ExperimentalTime::class)
private fun groupProtestsByDate(
    protests: List<UserProtestResource>
): Map<LocalDate, List<UserProtestResource>> {
    return protests
        .groupBy { it.date }
        .toSortedMap(compareByDescending { it })
        .mapValues { (_, protestsForDate) ->
            protestsForDate.sortedBy { it.startAt }
        }
}
