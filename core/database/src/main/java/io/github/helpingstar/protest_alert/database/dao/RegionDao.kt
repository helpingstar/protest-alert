package io.github.helpingstar.protest_alert.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.helpingstar.protest_alert.database.model.RegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDao {
    @Query(
        value = """
        SELECT * FROM regions
        WHERE id = :regionId
        """
    )
    fun getRegionEntity(regionId: Long): Flow<RegionEntity>

    @Query(value = "SELECT * FROM regions")
    fun getRegionEntities(): Flow<List<RegionEntity>>

    @Upsert
    suspend fun upsertRegions(entities: List<RegionEntity>)

    @Query(
        value = """
            DELETE FROM regions
            WHERE id in (:ids)
        """
    )
    suspend fun deleteRegions(ids: List<Long>)
}