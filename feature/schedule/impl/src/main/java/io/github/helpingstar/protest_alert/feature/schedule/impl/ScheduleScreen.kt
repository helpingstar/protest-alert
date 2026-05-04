package io.github.helpingstar.protest_alert.feature.schedule.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.model.data.Region
import io.github.helpingstar.protest_alert.core.model.data.UserData
import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState
import io.github.helpingstar.protest_alert.core.ui.RegionsTabContent
import io.github.helpingstar.protest_alert.core.util.getKoreanDayOfWeek
import io.github.helpingstar.protest_alert.feature.schedule.impl.component.DateChip
import io.github.helpingstar.protest_alert.feature.schedule.impl.component.DateChipVariant
import io.github.helpingstar.protest_alert.feature.schedule.impl.component.EmptyStateContainer
import io.github.helpingstar.protest_alert.feature.schedule.impl.component.ScheduleItem
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.math.log10
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val onboardingUiState by viewModel.onboardingUiState.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val followedRegionNames by viewModel.followedRegionNames.collectAsStateWithLifecycle()

    ScheduleScreen(
        isSyncing = isSyncing,
        onboardingUiState = onboardingUiState,
        feedState = feedState,
        followedRegionNames = followedRegionNames,
        onRegionCheckedChanged = viewModel::updateRegionSelection,
        saveFollowedRegions = viewModel::dismissOnboarding,
        modifier = modifier
    )
}

@Composable
internal fun ScheduleScreen(
    isSyncing: Boolean,
    onboardingUiState: OnboardingUiState,
    feedState: ProtestFeedUiState,
    followedRegionNames: List<String>,
    onRegionCheckedChanged: (String, Boolean) -> Unit,
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
                followedRegionNames = followedRegionNames,
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
    followedRegionNames: List<String>,
    modifier: Modifier = Modifier
) {
    when (feedState) {
        ProtestFeedUiState.Loading -> Unit
        is ProtestFeedUiState.Success -> {
            if (feedState.feed.isEmpty()) {
                EmptyStateContainer(
                    followedRegionNames = followedRegionNames,
                    modifier = modifier.fillMaxWidth()
                )
            } else {
                val groupedProtests = groupProtestsByDate(feedState.feed)

                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                ) {
                    groupedProtests.forEach { (date, protestsForDate) ->
                        item(key = "header_$date") {
                            DateHeader(date = date)
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        itemsIndexed(
                            items = protestsForDate,
                            key = { _, protest -> protest.id }
                        ) { index, protest ->
                            ScheduleItem(protest = protest)
                            if (index < protestsForDate.size - 1) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateHeader(
    date: LocalDate,
    today: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date,
    modifier: Modifier = Modifier
) {
    val datePart = "${date.year}년 ${String.format(Locale.ROOT, "%02d", date.month.number)}월 ${
        String.format(
            Locale.ROOT, "%02d",
            date.day
        )
    }일 ("
    val dayOfWeek = date.getKoreanDayOfWeek()

    val isToday = date == today
    val isTomorrow = date == today.plus(1, DateTimeUnit.DAY)
    val isSaturday = date.dayOfWeek.ordinal == 5
    val isSunday = date.dayOfWeek.ordinal == 6
    val dateChipVariant = when {
        isToday -> DateChipVariant.TODAY
        isTomorrow -> DateChipVariant.TOMORROW
        else -> null
    }

    val dayOfWeekColor = when {
        isSunday -> Color(0xFFE11D48)  // Red (same as DateChip today background)
        isSaturday -> Color(0xFF2B7FFF) // Blue (same as primary color)
        else -> MaterialTheme.colorScheme.onBackground
    }

    val annotatedString = buildAnnotatedString {
        append(datePart)
        withStyle(style = SpanStyle(color = dayOfWeekColor)) {
            append(dayOfWeek)
        }
        append(")")
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        dateChipVariant?.let { variant ->
            DateChip(variant = variant)
        }
    }
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

    val userData = UserData(
        followedRegions = setOf("seoul"),
        shouldHideOnboarding = false,
        updateNotificationEnabled = false,
    )
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

    PaTheme {
        ScheduleScreen(
            isSyncing = false,
            onboardingUiState = OnboardingUiState.Shown(regions = sampleRegions),
            feedState = ProtestFeedUiState.Success(feed = sampleProtests),
            followedRegionNames = listOf("서울", "대구"),
            onRegionCheckedChanged = { _, _ -> },
            saveFollowedRegions = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleScreenOnlyContentPreview() {
    val userData = UserData(
        followedRegions = setOf("seoul", "busan"),
        shouldHideOnboarding = true,
        updateNotificationEnabled = false,
    )
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

    PaTheme {
        ScheduleScreen(
            isSyncing = false,
            onboardingUiState = OnboardingUiState.NotShown,
            feedState = ProtestFeedUiState.Success(feed = sampleProtests),
            followedRegionNames = listOf("서울", "부산"),
            onRegionCheckedChanged = { _, _ -> },
            saveFollowedRegions = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateHeaderTodayPreview() {
    val testDate = LocalDate(2025, 2, 2)  // Sunday
    PaTheme {
        DateHeader(
            date = testDate,
            today = testDate  // Make it "today"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateHeaderSaturdayPreview() {
    val testDate = LocalDate(2025, 2, 1)  // Saturday
    PaTheme {
        DateHeader(
            date = testDate,
            today = LocalDate(2025, 2, 2)  // Not today
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateHeaderSundayPreview() {
    val testDate = LocalDate(2025, 2, 2)  // Sunday
    PaTheme {
        DateHeader(
            date = testDate,
            today = LocalDate(2025, 2, 3)  // Not today
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateHeaderWeekdayPreview() {
    val testDate = LocalDate(2025, 2, 3)  // Monday
    PaTheme {
        DateHeader(
            date = testDate,
            today = LocalDate(2025, 2, 4)  // Not today
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateHeaderTomorrowPreview() {
    val testDate = LocalDate(2025, 2, 4)  // Tuesday
    PaTheme {
        DateHeader(
            date = testDate,
            today = LocalDate(2025, 2, 3)  // Make it "tomorrow"
        )
    }
}
