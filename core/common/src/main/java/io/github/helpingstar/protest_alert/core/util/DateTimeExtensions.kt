package io.github.helpingstar.protest_alert.core.util

import kotlinx.datetime.LocalDate

fun LocalDate.getKoreanDayOfWeek(): String {
    val days = listOf("월", "화", "수", "목", "금", "토", "일")
    return days[dayOfWeek.ordinal]
}