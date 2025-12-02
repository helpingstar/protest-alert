package io.github.helpingstar.protest_alert.database.util

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal class JsonObjectConverter {
    @TypeConverter
    fun stringToJsonObject(value: String?): JsonObject? =
        value?.let { Json.parseToJsonElement(it).jsonObject }

    @TypeConverter
    fun jsonObjectToString(value: JsonObject?): String? =
        value?.toString()
}
