package io.github.helpingstar.protest_alert.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProtestResourceDao {
    @Query(
        value = """
            SELECT * FROM protest_resources
    """,
    )
    fun getProtestResources(
    ): Flow<List<ProtestResourceEntity>>
}