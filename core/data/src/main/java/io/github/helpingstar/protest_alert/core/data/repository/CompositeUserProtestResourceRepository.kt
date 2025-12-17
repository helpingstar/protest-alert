package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import io.github.helpingstar.protest_alert.core.model.data.mapToUserProtestResources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompositeUserProtestResourceRepository @Inject constructor(
    val protestRepository: ProtestRepository,
    val userDataRepository: UserDataRepository,
) : UserProtestResourceRepository {

    override fun observeAll(
        query: ProtestResourceQuery
    ): Flow<List<UserProtestResource>> =
        protestRepository.getProtestResources(query)
            .combine(userDataRepository.userData) { protestResources, userData ->
                protestResources.mapToUserProtestResources(userData)
            }

    override fun observeAllForFollowedRegions(): Flow<List<UserProtestResource>> =
        userDataRepository.userData.map { it.unfollowedRegions }.distinctUntilChanged()
            .flatMapLatest { unfollowedTopics ->
                when {
                    unfollowedTopics.isEmpty() -> observeAll(ProtestResourceQuery())
                    else -> observeAll(ProtestResourceQuery(filterRegionIds = unfollowedTopics))
                }
            }
}