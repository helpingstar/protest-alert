package io.github.helpingstar.protest_alert.data.repository

import io.github.helpigstar.core.datastore.test.InMemoryDataStore
import io.github.helpingstar.protest_alert.core.data.Synchronizer
import io.github.helpingstar.protest_alert.core.data.model.asEntity
import io.github.helpingstar.protest_alert.core.data.model.regionEntityShells
import io.github.helpingstar.protest_alert.core.data.repository.OfflineFirstProtestRepository
import io.github.helpingstar.protest_alert.core.datastore.PaPreferencesDataSource
import io.github.helpingstar.protest_alert.core.datastore.UserPreferences
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.network.model.NetworkChangeList
import io.github.helpingstar.protest_alert.core.network.model.NetworkProtestResource
import io.github.helpingstar.protest_alert.core.testing.notifications.TestNotifier
import io.github.helpingstar.protest_alert.data.testdoubles.CollectionType
import io.github.helpingstar.protest_alert.data.testdoubles.TestPaNetworkDataSource
import io.github.helpingstar.protest_alert.data.testdoubles.TestProtestResourceDao
import io.github.helpingstar.protest_alert.data.testdoubles.TestRegionDao
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import io.github.helpingstar.protest_alert.database.model.RegionEntity
import io.github.helpingstar.protest_alert.database.model.asExternalModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OfflineFirstProtestRepositoryTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: OfflineFirstProtestRepository

    private lateinit var paPreferencesDataSource: PaPreferencesDataSource

    private lateinit var protestResourceDao: TestProtestResourceDao

    private lateinit var regionDao: TestRegionDao

    private lateinit var network: TestPaNetworkDataSource

    private lateinit var notifier: TestNotifier

    private lateinit var synchronizer: Synchronizer

    @Before
    fun setup() {
        paPreferencesDataSource = PaPreferencesDataSource(
            InMemoryDataStore(UserPreferences.getDefaultInstance()),
        )
        protestResourceDao = TestProtestResourceDao()
        regionDao = TestRegionDao()
        network = TestPaNetworkDataSource()
        notifier = TestNotifier()
        synchronizer = TestSynchronizer(
            paPreferences = paPreferencesDataSource,
        )

        subject = OfflineFirstProtestRepository(
            paPreferencesDataSource = paPreferencesDataSource,
            protestResourceDao = protestResourceDao,
            regionDao = regionDao,
            network = network,
            notifier = notifier,
        )
    }

    @Test
    fun offlineFirstProtestRepository_protest_resources_stream_is_backed_by_protest_resource_dao() =
        testScope.runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                protestResourceDao.getProtestResources()
                    .first()
                    .map(ProtestResourceEntity::asExternalModel),
                subject.getProtestResources()
                    .first()
            )
        }

    @Test
    fun offlineFirstProtestRepository_sync_pulls_from_network() =
        testScope.runTest {
            paPreferencesDataSource.setShouldHideOnboarding(false)

            subject.syncWith(synchronizer)

            val protestResourcesFromNetwork = network.getProtestResources()
                .map(NetworkProtestResource::asEntity)
                .map(ProtestResourceEntity::asExternalModel)

            val protestResourcesFromDb = protestResourceDao.getProtestResources()
                .first()
                .map(ProtestResourceEntity::asExternalModel)

            assertEquals(
                protestResourcesFromNetwork.map(ProtestResource::id).sorted(),
                protestResourcesFromDb.map(ProtestResource::id).sorted(),
            )

            assertEquals(
                network.latestChangeListVersion(CollectionType.ProtestResources),
                actual = synchronizer.getLastUpdatedAt().protestResourceLastUpdatedAt,
            )

            assertTrue(notifier.addedProtestResources.isEmpty())
        }

    @Test
    fun offlineFirstProtestRepository_sync_deletes_items_marked_deleted_on_network() =
        testScope.runTest {
            paPreferencesDataSource.setShouldHideOnboarding(false)

            val protestResourcesFromNetwork = network.getProtestResources()
                .map(NetworkProtestResource::asEntity)
                .map(ProtestResourceEntity::asExternalModel)

            val deletedItems = protestResourcesFromNetwork
                .map(ProtestResource::id)
                .partition { it % 2L == 0L }
                .first
                .toSet()

            deletedItems.forEach {
                network.editCollection(
                    collectionType = CollectionType.ProtestResources,
                    id = it.toString(),
                    lastUpdatedAt = network.latestChangeListVersion(CollectionType.ProtestResources)
                        .plus(1, DateTimeUnit.SECOND),
                    isDelete = true,
                )
            }

            subject.syncWith(synchronizer)

            val protestResourceFromDb = protestResourceDao.getProtestResources()
                .first()
                .map(ProtestResourceEntity::asExternalModel)

            assertEquals(
                expected = (protestResourcesFromNetwork.map(ProtestResource::id) - deletedItems).sorted(),
                actual = protestResourceFromDb.map(ProtestResource::id).sorted()
            )

            assertEquals(
                expected = network.latestChangeListVersion(CollectionType.ProtestResources),
                actual = synchronizer.getLastUpdatedAt().protestResourceLastUpdatedAt,
            )

            assertTrue(notifier.addedProtestResources.isEmpty())
        }

    @Test
    fun offlineFirstProtestRepository_incremental_sync_pulls_from_network() =
        testScope.runTest {
            // User has not onboarded
            paPreferencesDataSource.setShouldHideOnboarding(false)

            val currentLastUpdatedAt = network.getProtestResourceChangeList()
                .first()
                .lastUpdatedAt

            // Pretend that we already have the first change
            synchronizer.updateChangeListVersions {
                copy(protestResourceLastUpdatedAt = currentLastUpdatedAt)
            }

            subject.syncWith(synchronizer)

            val changeList = network.changeListsAfter(
                CollectionType.ProtestResources,
                lastUpdatedAt = currentLastUpdatedAt,
            )
            val changeListIds = changeList
                .map(NetworkChangeList::id)
                .toSet()

            val protestResourcesFromNetwork = network.getProtestResources()
                .map(NetworkProtestResource::asEntity)
                .map(ProtestResourceEntity::asExternalModel)
                .filter { it.id.toString() in changeListIds }

            val protestResourcesFromDb = protestResourceDao.getProtestResources()
                .first()
                .map(ProtestResourceEntity::asExternalModel)

            assertEquals(
                expected = protestResourcesFromNetwork.map(ProtestResource::id).sorted(),
                actual = protestResourcesFromDb.map(ProtestResource::id).sorted(),
            )

            // After sync last updated at should be updated
            assertEquals(
                expected = changeList.maxOf(NetworkChangeList::lastUpdatedAt),
                actual = synchronizer.getLastUpdatedAt().protestResourceLastUpdatedAt,
            )

            // Notifier should not have been called
            assertTrue(notifier.addedProtestResources.isEmpty())
        }

    @Test
    fun offlineFirstProtestRepository_sync_saves_shell_region_entities() =
        testScope.runTest {
            subject.syncWith(synchronizer)

            assertEquals(
                expected = network.getProtestResources()
                    .map(NetworkProtestResource::regionEntityShells)
                    .distinctBy(RegionEntity::id)
                    .sortedBy(RegionEntity::toString),
                actual = regionDao.getRegionEntities()
                    .first()
                    .sortedBy(RegionEntity::toString),
            )
        }

    @Test
    fun offlineFirstProtestRepository_sends_notifications_for_newly_synced_protests_that_are_followed() =
        testScope.runTest {
            // User has onboarded
            paPreferencesDataSource.setShouldHideOnboarding(true)

            // User has enabled update notifications
            paPreferencesDataSource.setUpdateNotificationEnabled(true)

            val networkProtestResources = network.getProtestResources()

            // Follow roughly half the regions
            val followedRegionIds = networkProtestResources
                .map(NetworkProtestResource::region)
                .distinct()
                .filterIndexed { index, _ -> index % 2 == 0 }
                .toSet()

            // Set followed regions
            paPreferencesDataSource.setFollowedRegionIds(followedRegionIds)

            subject.syncWith(synchronizer)

            val followedProtestResourceIdsFromNetwork = networkProtestResources
                .filter { it.region in followedRegionIds }
                .map(NetworkProtestResource::id)
                .sorted()

            // Notifier should have been called with only protest resources that have regions
            // that the user follows
            assertEquals(
                expected = followedProtestResourceIdsFromNetwork,
                actual = notifier.addedProtestResources.first().map(ProtestResource::id).sorted(),
            )
        }

    @Test
    fun offlineFirstProtestRepository_does_not_send_notifications_for_existing_protest_resources() =
        testScope.runTest {
            // User has onboarded
            paPreferencesDataSource.setShouldHideOnboarding(true)

            // User has enabled update notifications
            paPreferencesDataSource.setUpdateNotificationEnabled(true)

            val networkProtestResources = network.getProtestResources()
                .map(NetworkProtestResource::asEntity)

            val protestResources = networkProtestResources
                .map(ProtestResourceEntity::asExternalModel)

            // Prepopulate dao with protest resources
            protestResourceDao.upsertProtestResources(networkProtestResources)

            val followedRegionIds = protestResources
                .map(ProtestResource::region)
                .toSet()

            // Follow all regions
            paPreferencesDataSource.setFollowedRegionIds(followedRegionIds)

            subject.syncWith(synchronizer)

            // Notifier should not have been called because all protest resources existed previously
            assertTrue(notifier.addedProtestResources.isEmpty())
        }


}