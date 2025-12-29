package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface UserProtestResourceRepository {
    fun observeAll(
        query: ProtestResourceQuery = ProtestResourceQuery(
            filterRegionIds = null,
            filterNewsIds = null,
        ),
    ): Flow<List<UserProtestResource>>

    fun observeAllForFollowedRegions(
        sinceDate: LocalDate? = null,
    ): Flow<List<UserProtestResource>>
}