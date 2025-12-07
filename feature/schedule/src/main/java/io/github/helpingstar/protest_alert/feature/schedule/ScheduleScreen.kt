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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.ui.ProtestFeedUiState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.ExperimentalTime

// Colors from Figma design
private val TextPrimary = Color(0xFF171A1F)
private val TextSecondary = Color(0xFF565D6D)
private val TagBackground = Color(0x1A3899FA) // 10% opacity
private val TagText = Color(0xFF3899FA)
private val CardBackground = Color(0x99F3F4F6) // 60% opacity

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

@Composable
private fun ScheduleContent(
    protests: List<ProtestResource>,
    modifier: Modifier = Modifier
) {
    // TODO(human): Implement groupProtestsByDate function
    val groupedProtests = groupProtestsByDate(protests)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        groupedProtests.forEach { (date, protestsForDate) ->
            item(key = "header_$date") {
                DateHeader(date = date)
            }

            items(
                items = protestsForDate,
                key = { it.id }
            ) { protest ->
                ScheduleItem(protest = protest)
            }
        }
    }
}

@Composable
private fun DateHeader(
    date: LocalDate,
    modifier: Modifier = Modifier
) {
    @Suppress("DEPRECATION")
    val formattedDate = "${date.year}년 ${String.format(Locale.ROOT, "%02d", date.monthNumber)}월 ${String.format(Locale.ROOT, "%02d", date.dayOfMonth)}일"

    Text(
        text = formattedDate,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ScheduleItem(
    protest: ProtestResource,
    modifier: Modifier = Modifier
) {
    val timeText = formatTimeRange(protest)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Time column
                Text(
                    text = timeText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    modifier = Modifier.width(80.dp),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Location
                Text(
                    text = protest.location ?: "장소 미정",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(96.dp))

                // Participants icon and count
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TextSecondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "참여자 ${formatParticipants(protest.participants)}명",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.weight(1f))

                // Region tag
                RegionTag(region = protest.region)
            }
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
            .background(
                color = TagBackground,
                shape = RoundedCornerShape(11.dp)
            )
            .padding(horizontal = 12.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = region,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TagText
        )
    }
}

@OptIn(ExperimentalTime::class)
private fun formatTimeRange(protest: ProtestResource): String {
    val timeZone = TimeZone.of("Asia/Seoul")

    val startTime = protest.startAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    val endTime = protest.endAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    return "$startTime ~\n$endTime"
}

private fun formatParticipants(count: Int?): String {
    return count?.toString() ?: "0"
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
