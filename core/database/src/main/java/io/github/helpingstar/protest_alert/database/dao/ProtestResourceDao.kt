package io.github.helpingstar.protest_alert.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

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
                AND CASE WHEN :useFilterSinceDate
                    THEN date >= :sinceDate
                    ELSE 1
                END
                AND CASE WHEN :useFilterProtestResourceIds
                    THEN id IN (:filterProtestResourceIds)
                    ELSE 1
                END
    """,
    )
    fun getProtestResources(
        useFilterRegionIds: Boolean = false,
        filterRegionIds: Set<String> = emptySet(),
        useFilterSinceDate: Boolean = false,
        sinceDate: LocalDate = LocalDate.fromEpochDays(0),
        useFilterProtestResourceIds: Boolean = false,
        filterProtestResourceIds: Set<Long> = emptySet(),
    ): Flow<List<ProtestResourceEntity>>

    @Query(
        value = """
            SELECT id FROM protest_resources
            WHERE
                CASE WHEN :useFilterRegionIds
                    THEN region IN (:filterRegionIds)
                    ELSE 1
                END
                AND CASE WHEN :useFilterProtestResourceIds
                    THEN id IN (:filterProtestResourceIds)
                    ELSE 1
                END
    """,
    )
    fun getProtestResourceIds(
        useFilterRegionIds: Boolean = false,
        filterRegionIds: Set<String> = emptySet(),
        useFilterProtestResourceIds: Boolean = false,
        filterProtestResourceIds: Set<Long> = emptySet(),
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