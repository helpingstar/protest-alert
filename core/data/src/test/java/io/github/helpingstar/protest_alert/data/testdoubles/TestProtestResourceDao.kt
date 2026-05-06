package io.github.helpingstar.protest_alert.data.testdoubles

import io.github.helpingstar.protest_alert.database.dao.ProtestResourceDao
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class TestProtestResourceDao : ProtestResourceDao {
    private val entitiesStateFlow = MutableStateFlow(emptyList<ProtestResourceEntity>())

    override fun getProtestResources(
        useFilterRegionIds: Boolean,
        filterRegionIds: Set<String>,
        useFilterSinceDate: Boolean,
        sinceDate: LocalDate,
        useFilterProtestResourceIds: Boolean,
        filterProtestResourceIds: Set<Long>,
    ): Flow<List<ProtestResourceEntity>> =
        entitiesStateFlow.map { entities ->
            var result = entities
            if (useFilterRegionIds) {
                result = result.filter { it.region in filterRegionIds }
            }

            if (useFilterSinceDate) {
                result = result.filter { it.date >= sinceDate }
            }

            if (useFilterProtestResourceIds) {
                result = result.filter { it.id in filterProtestResourceIds }
            }

            result
        }

    override fun getProtestResourceIds(
        useFilterRegionIds: Boolean,
        filterRegionIds: Set<String>,
        useFilterProtestResourceIds: Boolean,
        filterProtestResourceIds: Set<Long>,
    ): Flow<List<Long>> =
        entitiesStateFlow.map { entities ->
            var result = entities
            if (useFilterRegionIds) {
                result = result.filter { it.region in filterRegionIds }
            }

            if (useFilterProtestResourceIds) {
                result = result.filter { it.id in filterProtestResourceIds }
            }

            result.map { it.id }
        }

    override suspend fun upsertProtestResources(protestResourceEntities: List<ProtestResourceEntity>) {
        entitiesStateFlow.update { oldValues ->
            (protestResourceEntities + oldValues)
                .distinctBy(ProtestResourceEntity::id)
                .sortedWith(
                    compareBy(ProtestResourceEntity::date)
                )
        }
    }

    override suspend fun deleteProtestResources(ids: List<String>) {
        val idSet = ids.mapNotNull(String::toLongOrNull).toSet()
        entitiesStateFlow.update { entities ->
            entities.filterNot { it.id in idSet }
        }
    }
}