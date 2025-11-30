package io.github.helpingstar.protest_alert.feature.schedule

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.helpingstar.protest_alert.feature.schedule.model.DailySchedule
import io.github.helpingstar.protest_alert.feature.schedule.model.ScheduleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor() : ViewModel() {

    private val _dailySchedules = MutableStateFlow(getSampleSchedules())
    val dailySchedules: StateFlow<List<DailySchedule>> = _dailySchedules.asStateFlow()

    private fun getSampleSchedules(): List<DailySchedule> {
        return listOf(
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
                    ),
                    ScheduleItem(
                        id = "4",
                        date = "2024-07-23",
                        startTime = "10:30",
                        endTime = "13:30",
                        location = "인천시청 앞",
                        participantCount = 950,
                        region = "인천"
                    )
                )
            ),
            DailySchedule(
                date = "2024-07-24",
                schedules = listOf(
                    ScheduleItem(
                        id = "5",
                        date = "2024-07-24",
                        startTime = "08:00",
                        endTime = "11:00",
                        location = "대구 동성로",
                        participantCount = 400,
                        region = "대구"
                    )
                )
            ),
            DailySchedule(
                date = "2024-07-25",
                schedules = listOf(
                    ScheduleItem(
                        id = "6",
                        date = "2024-07-25",
                        startTime = "11:00",
                        endTime = "14:00",
                        location = "부산 해운대 해변",
                        participantCount = 3000,
                        region = "부산"
                    ),
                    ScheduleItem(
                        id = "7",
                        date = "2024-07-25",
                        startTime = "15:00",
                        endTime = "18:00",
                        location = "광주 5.18 민주광장",
                        participantCount = 600,
                        region = "광주"
                    ),
                    ScheduleItem(
                        id = "8",
                        date = "2024-07-25",
                        startTime = "19:00",
                        endTime = "21:00",
                        location = "서울역 광장",
                        participantCount = 12000,
                        region = "서울"
                    )
                )
            ),
            DailySchedule(
                date = "2024-07-26",
                schedules = listOf(
                    ScheduleItem(
                        id = "9",
                        date = "2024-07-26",
                        startTime = "13:00",
                        endTime = "16:00",
                        location = "대전역 동광장",
                        participantCount = 200,
                        region = "대전"
                    )
                )
            )
        )
    }
}
