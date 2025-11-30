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
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.feature.schedule.model.DailySchedule
import io.github.helpingstar.protest_alert.feature.schedule.model.ScheduleItem

private val CardBackgroundColor = Color(0xFFF3F4F6)
private val TextPrimaryColor = Color(0xFF171A1F)
private val TextSecondaryColor = Color(0xFF565D6D)
private val TagBackgroundColor = Color(0x1A3899FA)
private val TagTextColor = Color(0xFF3899FA)

@Composable
internal fun ScheduleRoute(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val dailySchedules by viewModel.dailySchedules.collectAsState()

    ScheduleScreen(
        dailySchedules = dailySchedules,
        modifier = modifier
    )
}

@Composable
internal fun ScheduleScreen(
    dailySchedules: List<DailySchedule>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        dailySchedules.forEach { dailySchedule ->
            item {
                DateHeader(date = dailySchedule.date)
            }
            items(
                items = dailySchedule.schedules,
                key = { it.id }
            ) { schedule ->
                ScheduleCard(schedule = schedule)
            }
        }
    }
}

@Composable
private fun DateHeader(
    date: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = date,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun ScheduleCard(
    schedule: ScheduleItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color(0x14171A1F),
                spotColor = Color(0x14171A1F)
            ),
        shape = RoundedCornerShape(10.dp),
        color = CardBackgroundColor.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // 시간
                Text(
                    text = "${schedule.startTime} ~\n${schedule.endTime}",
                    color = TextPrimaryColor,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.width(80.dp)
                )

                // 장소 및 참여자 정보
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = schedule.location,
                        color = TextPrimaryColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = "참여자",
                            modifier = Modifier.size(16.dp),
                            tint = TextSecondaryColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "참여자 ${formatParticipantCount(schedule.participantCount)}명",
                            color = TextSecondaryColor,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                // 지역 태그
                RegionTag(region = schedule.region)
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
                color = TagBackgroundColor,
                shape = RoundedCornerShape(11.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = region,
            color = TagTextColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

private fun formatParticipantCount(count: Int): String {
    return count.toString()
}

@Preview(showBackground = true)
@Composable
private fun ScheduleScreenPreview() {
    val sampleSchedules = listOf(
        DailySchedule(
            date = "2024-07-22",
            schedules = listOf(
                ScheduleItem(
                    id = "1",
                    date = "2024-07-22",
                    startTime = "09:00",
                    endTime = "12:00",
                    location = "국회의사당역 3번 출구 앞",
                    participantCount = 7000,
                    region = "서울"
                ),
                ScheduleItem(
                    id = "2",
                    date = "2024-07-22",
                    startTime = "14:00",
                    endTime = "17:00",
                    location = "강남역 10번 출구",
                    participantCount = 1500,
                    region = "서울"
                )
            )
        ),
        DailySchedule(
            date = "2024-07-23",
            schedules = listOf(
                ScheduleItem(
                    id = "3",
                    date = "2024-07-23",
                    startTime = "10:00",
                    endTime = "13:00",
                    location = "수원역 광장",
                    participantCount = 800,
                    region = "경기남부"
                )
            )
        )
    )

    PaTheme {
        ScheduleScreen(dailySchedules = sampleSchedules)
    }
}
