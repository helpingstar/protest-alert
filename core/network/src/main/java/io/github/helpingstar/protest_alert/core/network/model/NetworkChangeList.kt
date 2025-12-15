package io.github.helpingstar.protest_alert.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class NetworkChangeList(
    val id: Long,
    @SerialName("change_list_version")
    val lastUpdatedAt: Instant,
    @SerialName("is_delete")
    val isDelete: Boolean
)