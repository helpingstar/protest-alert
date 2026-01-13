package io.github.helpingstar.protest_alert.feature.schedule.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.model.data.Region
import io.github.helpingstar.protest_alert.core.model.data.UserData
import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState
import io.github.helpingstar.protest_alert.core.ui.RegionsTabContent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.math.log10
import kotlin.time.Instant

private val TagBackground = Color(0x1A3899FA) // rgba(56,153,250,0.1)


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
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
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
        AnimatedVisibility(
            visible = isSyncing || isFeedLoading || isOnboardingLoading,
            enter = slideInVertically(
                initialOffsetY = { it }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it }
            ) + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
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
            Column {
                RegionsTabContent(
                    title = "관심 지역 선택 후 완료 버튼을 눌러주세요",
                    regions = onboardingUiState.regions,
                    onFollowButtonClick = onRegionCheckedChanged,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = saveFollowedRegions,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PaColor.accentPrimary,
                            contentColor = PaColor.textOnAccent,
                            disabledContainerColor = PaColor.backgroundDisabled,
                            disabledContentColor = PaColor.textDisabled
                        ),
                        shape = RoundedCornerShape(16.dp),
                        enabled = onboardingUiState.isDismissable,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        contentPadding = ButtonDefaults.ContentPadding,
                    ) {
                        Text(
                            text = "완료",
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp,
                        )
                    }
                }
            }

        }
    }
}


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
        color = PaColor.textPrimary,
        lineHeight = 28.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 11.dp)
    )
}


@Composable
private fun ScheduleItem(
    protest: UserProtestResource,
    modifier: Modifier = Modifier
) {
    val (startTime, endTime) = formatTimeRangePair(protest)
    val calculatedAlpha = expToLinear(protest.participants ?: 0)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = PaColor.surfaceCard.copy(alpha = calculatedAlpha.toFloat())
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
                    color = PaColor.textPrimary,
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
                    color = PaColor.textPrimary,
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
                        tint = PaColor.textSecondary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "참여자 ${formatParticipantsWithComma(protest.participants)}명",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        color = PaColor.textSecondary,
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
            .height(28.dp)
            .background(
                color = TagBackground,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = region,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            color = PaColor.accentPrimary,
            lineHeight = 20.sp
        )
    }
}


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

fun expToLinear(value: Int, max: Int = 100_000, minOut: Double = 0.2): Double {
    require(minOut in 0.0..1.0) { "minOut must be within [0.0, 1.0]" }

    val clamped = value.coerceIn(0, max)

    // 0은 로그 계산 불가 -> 하한으로 매핑
    if (clamped == 0) return minOut.coerceIn(0.0, 1.0)

    val t = log10(clamped.toDouble()) / log10(max.toDouble())
    val out = minOut + t * (1.0 - minOut)
    return out.coerceIn(minOut, 1.0)
}

@Preview(showBackground = true)
@Composable
private fun ScheduleScreenWithOnboardingPreview() {
    val sampleRegions = listOf(
        FollowableRegion(
            region = Region(id = "seoul", name = "서울", createdAt = Instant.DISTANT_PAST),
            isFollowed = true
        ),
        FollowableRegion(
            region = Region(id = "busan", name = "부산", createdAt = Instant.DISTANT_PAST),
            isFollowed = false
        ),
        FollowableRegion(
            region = Region(id = "daegu", name = "대구", createdAt = Instant.DISTANT_PAST),
            isFollowed = true
        ),
    )

    val userData = UserData(followedRegions = setOf("seoul"), shouldHideOnboarding = false)
    val sampleProtests = listOf(
        ProtestResource(
            id = 1L,
            date = LocalDate(2025, 1, 15),
            startAt = Instant.parse("2025-01-15T01:00:00Z"),
            endAt = Instant.parse("2025-01-15T03:00:00Z"),
            location = "서울시청 앞 광장",
            participants = 1500,
            additionalInfo = null,
            createdAt = Instant.DISTANT_PAST,
            region = "서울",
            updatedAt = Instant.DISTANT_PAST
        ),
        ProtestResource(
            id = 2L,
            date = LocalDate(2025, 1, 15),
            startAt = Instant.parse("2025-01-15T05:00:00Z"),
            endAt = Instant.parse("2025-01-15T07:00:00Z"),
            location = "광화문 광장",
            participants = 3000,
            additionalInfo = null,
            createdAt = Instant.DISTANT_PAST,
            region = "서울",
            updatedAt = Instant.DISTANT_PAST
        ),
    ).map { UserProtestResource(it, userData) }

    ScheduleScreen(
        isSyncing = false,
        onboardingUiState = OnboardingUiState.Shown(regions = sampleRegions),
        onRegionCheckedChanged = { _, _ -> },
        feedState = ProtestFeedUiState.Success(feed = sampleProtests),
        saveFollowedRegions = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ScheduleScreenOnlyContentPreview() {
    val userData = UserData(followedRegions = setOf("seoul", "busan"), shouldHideOnboarding = true)
    val sampleProtests = listOf(
        ProtestResource(
            id = 1L,
            date = LocalDate(2025, 1, 15),
            startAt = Instant.parse("2025-01-15T01:00:00Z"),
            endAt = Instant.parse("2025-01-15T03:00:00Z"),
            location = "서울시청 앞 광장",
            participants = 1500,
            additionalInfo = null,
            createdAt = Instant.DISTANT_PAST,
            region = "서울",
            updatedAt = Instant.DISTANT_PAST
        ),
        ProtestResource(
            id = 2L,
            date = LocalDate(2025, 1, 15),
            startAt = Instant.parse("2025-01-15T05:00:00Z"),
            endAt = Instant.parse("2025-01-15T07:00:00Z"),
            location = "광화문 광장",
            participants = 3000,
            additionalInfo = null,
            createdAt = Instant.DISTANT_PAST,
            region = "서울",
            updatedAt = Instant.DISTANT_PAST
        ),
        ProtestResource(
            id = 3L,
            date = LocalDate(2025, 1, 14),
            startAt = Instant.parse("2025-01-14T04:00:00Z"),
            endAt = Instant.parse("2025-01-14T06:00:00Z"),
            location = "부산역 광장",
            participants = 800,
            additionalInfo = null,
            createdAt = Instant.DISTANT_PAST,
            region = "부산",
            updatedAt = Instant.DISTANT_PAST
        ),
    ).map { UserProtestResource(it, userData) }

    ScheduleScreen(
        isSyncing = false,
        onboardingUiState = OnboardingUiState.NotShown,
        onRegionCheckedChanged = { _, _ -> },
        feedState = ProtestFeedUiState.Success(feed = sampleProtests),
        saveFollowedRegions = {},
    )
}

