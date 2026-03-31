package io.github.helpingstar.protest_alert.core.domain

import io.github.helpingstar.protest_alert.core.domain.RegionSortField.NAME
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import io.github.helpingstar.protest_alert.core.model.data.Region
import io.github.helpingstar.protest_alert.core.testing.repository.TestRegionsRepository
import io.github.helpingstar.protest_alert.core.testing.repository.TestUserDataRepository
import io.github.helpingstar.protest_alert.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class GetFollowableRegionsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val regionsRepository = TestRegionsRepository()
    private val userDataRepository = TestUserDataRepository()

    val useCase = GetFollowableRegionsUseCase(
        regionsRepository,
        userDataRepository,
    )

    @Test
    fun whenNoParams_followableRegionsAreReturnedWithNoSorting() = runTest {
        val followableRegions = useCase()

        regionsRepository.sendRegions(testRegions)
        userDataRepository.setFollowedRegionIds(setOf(testRegions[0].id, testRegions[2].id))

        assertEquals(
            listOf(
                FollowableRegion(testRegions[0], true),
                FollowableRegion(testRegions[1], false),
                FollowableRegion(testRegions[2], true),
            ),
            followableRegions.first()
        )
    }

    @Test
    fun whenSortOrderIsByName_regionsSortedByNameAreReturned() = runTest {
        val followableRegions = useCase(
            sortBy = NAME,
        )

        regionsRepository.sendRegions(testRegions)
        userDataRepository.setFollowedRegionIds(setOf())

        assertEquals(
            followableRegions.first(),
            testRegions
                .sortedBy { it.name }
                .map {
                    FollowableRegion(it, false)
                },
        )
    }
}

private val testRegions = listOf(
    Region("1", "Seoul", Instant.fromEpochMilliseconds(0)),
    Region("2", "Busan", Instant.fromEpochMilliseconds(0)),
    Region("3", "Incheon", Instant.fromEpochMilliseconds(0)),
)