package io.github.helpingstar.protest_alert.core.model.data

import kotlinx.datetime.LocalDate
import kotlin.time.Instant


data class UserProtestResource internal constructor(
    val id: Long,
    val date: LocalDate,
    val startAt: Instant?,
    val endAt: Instant?,
    val location: String?,
    val participants: Int?,
    val additionalInfo: Map<String, String>?,
    val createdAt: Instant,
    val region: String,
    val updatedAt: Instant,
) {
    constructor(protestResource: ProtestResource, userData: UserData) : this(
        id = protestResource.id,
        date = protestResource.date,
        startAt = protestResource.startAt,
        endAt = protestResource.endAt,
        location = protestResource.location,
        participants = protestResource.participants,
        additionalInfo = protestResource.additionalInfo,
        createdAt = protestResource.createdAt,
        region = protestResource.region,
        updatedAt = protestResource.updatedAt
    )
}

fun List<ProtestResource>.mapToUserProtestResources(userData: UserData): List<UserProtestResource> =
    map { UserProtestResource(it, userData) }