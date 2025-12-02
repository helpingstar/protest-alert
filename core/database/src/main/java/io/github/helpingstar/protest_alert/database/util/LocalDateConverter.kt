package io.github.helpingstar.protest_alert.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

internal class LocalDateConverter {
    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? =
        value?.let(LocalDate::parse)

    @TypeConverter
    fun localDateToString(value: LocalDate?): String? =
        value?.toString()
}

