package io.github.helpingstar.protest_alert.feature.schedule

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


// Colors from Figma design
private val TextPrimary = Color(0xFF171A1F)
private val TextSecondary = Color(0xFF787878)
private val TagBackground = Color(0x1A3899FA) // rgba(56,153,250,0.1)
private val TagText = Color(0xFF3899FA)
private val CardBackground = Color(0x99F3F4F6) // rgba(243,244,246,0.6)

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
    when (feedState) {
        ProtestFeedUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ProtestFeedUiState.Success -> {
            ScheduleContent(
                protests = feedState.feed,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ScheduleContent(
    protests: List<ProtestResource>,
    modifier: Modifier = Modifier
) {
    val groupedProtests = groupProtestsByDate(protests)

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
    protest: ProtestResource,
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
private fun formatTimeRangePair(protest: ProtestResource): Pair<String, String> {
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
    protests: List<ProtestResource>
): Map<LocalDate, List<ProtestResource>> {
    return protests
        .groupBy { it.date }
        .toSortedMap(compareByDescending { it })
        .mapValues { (_, protestsForDate) ->
            protestsForDate.sortedBy { it.startAt }
        }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun ScheduleScreenLoadingPreview() {
    ScheduleScreen(
        feedState = ProtestFeedUiState.Loading
    )
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun ScheduleScreenSuccessPreview() {
    val sampleProtests = listOf(
        ProtestResource(
            id = 1,
            date = LocalDate(2024, 7, 26),
            startAt = Instant.fromEpochSeconds(1721955600), // 09:00 KST
            endAt = Instant.fromEpochSeconds(1721966400),   // 12:00 KST
            location = "국회의사당역 3번 출구 앞\n국회의사당역 3번 출구 앞\n국회의사당역 3번 출구 앞",
            participants = 7000,
            additionalInfo = null,
            createdAt = Instant.fromEpochSeconds(1721900000),
            region = "서울"
        ),
        ProtestResource(
            id = 2,
            date = LocalDate(2024, 7, 26),
            startAt = Instant.fromEpochSeconds(1721973600), // 14:00 KST
            endAt = Instant.fromEpochSeconds(1721984400),   // 17:00 KST
            location = "강남역 10번 출구",
            participants = 1500,
            additionalInfo = null,
            createdAt = Instant.fromEpochSeconds(1721900000),
            region = "서울"
        ),
        ProtestResource(
            id = 3,
            date = LocalDate(2024, 7, 25),
            startAt = Instant.fromEpochSeconds(1721872800), // 10:00 KST
            endAt = Instant.fromEpochSeconds(1721883600),   // 13:00 KST
            location = "수원역 광장",
            participants = 800,
            additionalInfo = null,
            createdAt = Instant.fromEpochSeconds(1721800000),
            region = "경기남부"
        )
    )
    ScheduleScreen(
        feedState = ProtestFeedUiState.Success(feed = sampleProtests)
    )
}
