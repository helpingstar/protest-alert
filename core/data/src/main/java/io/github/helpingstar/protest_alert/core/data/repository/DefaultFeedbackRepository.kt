package io.github.helpingstar.protest_alert.core.data.repository

import io.github.helpingstar.protest_alert.core.network.PaNetworkDataSource
import javax.inject.Inject

internal class DefaultFeedbackRepository @Inject constructor(
    private val networkDataSource: PaNetworkDataSource,
) : FeedbackRepository {
    override suspend fun submitFeedback(content: String): Result<Unit> = runCatching {
        networkDataSource.insertUserFeedback(content)
    }
}