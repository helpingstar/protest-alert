package io.github.helpingstar.protest_alert.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtestResourceDao {
    @Query(
        value = """
            SELECT * FROM protest_resources
            WHERE
                CASE WHEN :useFilterRegionIds
                    THEN region IN (:filterRegionIds)
                    ELSE 1
                END
    """,
    )
    fun getProtestResources(
        useFilterRegionIds: Boolean = false,
        filterRegionIds: Set<String> = emptySet(),
    ): Flow<List<ProtestResourceEntity>>

    @Query(
        value = """
            SELECT id FROM protest_resources
            WHERE
                CASE WHEN :useFilterRegionIds
                    THEN region IN (:filterRegionIds)
                    ELSE 1
                END
    """,
    )
    fun getProtestResourceIds(
        useFilterRegionIds: Boolean = false,
        filterRegionIds: Set<String> = emptySet(),
    ): Flow<List<Long>>

    @Upsert
    suspend fun upsertProtestResources(protestResourceEntities: List<ProtestResourceEntity>)

    @Query(
        value = """
            DELETE FROM protest_resources
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteProtestResources(ids: List<String>)
}