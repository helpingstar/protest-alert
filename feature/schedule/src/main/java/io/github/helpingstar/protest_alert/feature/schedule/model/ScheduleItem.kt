package io.github.helpingstar.protest_alert.feature.schedule.model

data class ScheduleItem(
    val id: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val participantCount: Int,
    val region: String
)

data class DailySchedule(
    val date: String,
    val schedules: List<ScheduleItem>
)
