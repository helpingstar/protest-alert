package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import kotlinx.coroutines.flow.Flow

interface UserProtestResourceRepository {
    fun observeAll(
        query: ProtestResourceQuery = ProtestResourceQuery(
            filterRegionIds = null,
            filterNewsIds = null,
        ),
    ): Flow<List<UserProtestResource>>

    fun observeAllForFollowedRegions(): Flow<List<UserProtestResource>>
}