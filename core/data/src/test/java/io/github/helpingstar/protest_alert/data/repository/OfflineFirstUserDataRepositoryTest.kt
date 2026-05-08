package io.github.helpingstar.protest_alert.data.repository

import io.github.helpigstar.core.datastore.test.InMemoryDataStore
import io.github.helpingstar.protest_alert.core.data.repository.OfflineFirstUserDataRepository
import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource
import io.github.helpingstar.protest_alert.core.datastore.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class OfflineFirstUserDataRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: OfflineFirstUserDataRepository

    private lateinit var paPreferencesDataSource: PaPreferencesDataSource

    @Before
    fun setup() {
        paPreferencesDataSource = PaPreferencesDataSource(
            InMemoryDataStore(UserPreferences.getDefaultInstance()),
        )

        subject = OfflineFirstUserDataRepository(
            paPreferencesDataSource = paPreferencesDataSource,
        )
    }

    @Test
    fun offlineFirstUserDataRepository_set_followed_regions_logic_delegates_to_pa_preferences() =
        testScope.runTest {
            subject.setFollowedRegionIds(followedRegionIds = setOf("1", "2"))

            assertEquals(
                setOf("1", "2"),
                subject.userData
                    .map { it.followedRegions }
                    .first(),
            )

            assertEquals(
                paPreferencesDataSource.userData
                    .map { it.followedRegions }
                    .first(),
                subject.userData
                    .map { it.followedRegions }
                    .first(),
            )
        }
}