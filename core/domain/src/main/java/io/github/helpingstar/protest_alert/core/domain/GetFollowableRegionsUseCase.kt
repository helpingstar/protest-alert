package io.github.helpingstar.protest_alert.core.domain

import io.github.helpingstar.protest_alert.core.data.repository.RegionsRepository
import io.github.helpingstar.protest_alert.core.data.repository.UserDataRepository
import io.github.helpingstar.protest_alert.core.domain.RegionSortField.NAME
import io.github.helpingstar.protest_alert.core.domain.RegionSortField.NONE
import io.github.helpingstar.protest_alert.core.model.data.FollowableRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFollowableRegionsUseCase @Inject constructor(
    private val regionsRepository: RegionsRepository,
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(sortBy: RegionSortField = NONE): Flow<List<FollowableRegion>> =
        combine(
            userDataRepository.userData,
            regionsRepository.getRegions()
        ) { userData, regions ->
            val followedRegions = regions
                .map { region ->
                    FollowableRegion(
                        region = region,
                        isFollowed = region.id !in userData.followedRegions,
                    )
                }
            when (sortBy) {
                NAME -> followedRegions.sortedBy { it.region.name }
                else -> followedRegions
            }
        }
}

enum class RegionSortField {
    NONE,
    NAME,
}