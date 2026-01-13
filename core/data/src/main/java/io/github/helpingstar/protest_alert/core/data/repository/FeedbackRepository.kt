package io.github.helpingstar.protest_alert.core.data.repository

interface FeedbackRepository {
    suspend fun submitFeedback(content: String): Result<Unit>
}