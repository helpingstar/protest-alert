package io.github.helpingstar.protest_alert.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkUserFeedback(
    val content: String,
)
