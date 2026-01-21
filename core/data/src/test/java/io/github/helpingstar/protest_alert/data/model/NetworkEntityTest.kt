package io.github.helpingstar.protest_alert.data.model

import io.github.helpingstar.protest_alert.core.data.model.asEntity
import io.github.helpingstar.protest_alert.core.model.data.Region
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.core.network.model.NetworkRegion
import io.github.helpingstar.protest_alert.core.network.model.asExternalModel
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class NetworkEntityTest {
    @Test
    fun networkRegionMapsToDatabaseModel() {
        val networkModel = NetworkRegion(
            id = 0,
            name = "Test",
            createdAt = Instant.fromEpochMilliseconds(0)
        )
        val entity = networkModel.asEntity()

        assertEquals("Test", entity.id)
        assertEquals("Test", entity.name)
        assertEquals(Instant.fromEpochMilliseconds(0), entity.createdAt)
    }

    val additionalInfo = JsonObject(
        mapOf(
            "key1" to JsonPrimitive("value1"),
            "key2" to JsonPrimitive(123),
            "key3" to JsonPrimitive(true)
        )
    )

    @Test
    fun networkProtestResourceMapsToDatabaseModel() {
        val networkModel = NetworkProtestResource(
            id = 0,
            date = LocalDate(2023, 1, 1),
            startAt = Instant.fromEpochMilliseconds(0),
            endAt = Instant.fromEpochMilliseconds(1),
            location = "Test",
            participants = 1,
            additionalInfo = additionalInfo,
            createdAt = Instant.fromEpochMilliseconds(2),
            region = "Test",
            updatedAt = Instant.fromEpochMilliseconds(3)
        )
        val entity = networkModel.asEntity()

        assertEquals(0, entity.id)
        assertEquals(LocalDate(2023, 1, 1), entity.date)
        assertEquals(Instant.fromEpochMilliseconds(0), entity.startAt)
        assertEquals(Instant.fromEpochMilliseconds(1), entity.endAt)
        assertEquals("Test", entity.location)
        assertEquals(1, entity.participants)
        assertEquals(additionalInfo, entity.additionalInfo)
        assertEquals(Instant.fromEpochMilliseconds(2), entity.createdAt)
        assertEquals("Test", entity.region)
        assertEquals(Instant.fromEpochMilliseconds(3), entity.updatedAt)
    }

    @Test
    fun networkRegionMapsToExternalModel() {
        val networkRegion = NetworkRegion(
            id = 0,
            name = "Test",
            createdAt = Instant.fromEpochMilliseconds(0)
        )

        val expected = Region(
            id = "Test",
            name = "Test",
            createdAt = Instant.fromEpochMilliseconds(0)
        )

        assertEquals(expected, networkRegion.asExternalModel())
    }
}