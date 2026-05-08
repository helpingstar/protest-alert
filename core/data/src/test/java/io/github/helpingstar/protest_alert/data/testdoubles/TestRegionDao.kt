package io.github.helpingstar.protest_alert.data.testdoubles

import io.github.helpingstar.protest_alert.database.dao.RegionDao
import io.github.helpingstar.protest_alert.database.model.RegionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TestRegionDao : RegionDao {
    private val entitiesStateFlow = MutableStateFlow(emptyList<RegionEntity>())

    override fun getRegionEntity(regionId: String): Flow<RegionEntity> =
        throw NotImplementedError("Unused in tests")

    override fun getRegionEntities(): Flow<List<RegionEntity>> = entitiesStateFlow

    override suspend fun insertOrIgnoreRegions(regionEntities: List<RegionEntity>): List<Long> {
        entitiesStateFlow.update { oldValues ->
            (oldValues + regionEntities).distinctBy(RegionEntity::id)
        }

        return regionEntities.mapIndexed { index, _ -> index.toLong() }
    }

    override suspend fun upsertRegions(entities: List<RegionEntity>) {
        entitiesStateFlow.update { oldValues ->
            (entities + oldValues).distinctBy(RegionEntity::id)
        }
    }

    override suspend fun deleteRegions(ids: List<String>) {
        val idSet = ids.toSet()
        entitiesStateFlow.update { entities ->
            entities.filterNot { it.id in idSet }
        }
    }
}