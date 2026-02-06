package io.github.helpingstar.protest_alert.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.helpingstar.protest_alert.core.data.repository.AnnouncementRepository
import io.github.helpingstar.protest_alert.core.data.repository.DefaultFeedbackRepository
import io.github.helpingstar.protest_alert.core.data.repository.FeedbackRepository
import io.github.helpingstar.protest_alert.core.data.repository.OfflineFirstAnnouncementRepository
import io.github.helpingstar.protest_alert.core.data.repository.OfflineFirstProtestRepository
import io.github.helpingstar.protest_alert.core.data.repository.OfflineFirstRegionsRepository
import io.github.helpingstar.protest_alert.core.data.repository.OfflineFirstUserDataRepository
import io.github.helpingstar.protest_alert.core.data.repository.ProtestRepository
import io.github.helpingstar.protest_alert.core.data.repository.RegionsRepository
import io.github.helpingstar.protest_alert.core.data.repository.UserDataRepository
import io.github.helpingstar.protest_alert.core.data.util.ConnectivityManagerNetworkMonitor
import io.github.helpingstar.protest_alert.core.data.util.NetworkMonitor

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsRegionRepository(
        regionRepository: OfflineFirstRegionsRepository,
    ): RegionsRepository

    @Binds
    internal abstract fun bindsProtestResourceRepository(
        protestRepository: OfflineFirstProtestRepository,
    ): ProtestRepository

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository
    ): UserDataRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor

    @Binds
    internal abstract fun bindsFeedbackRepository(
        feedbackRepository: DefaultFeedbackRepository,
    ): FeedbackRepository

    @Binds
    internal abstract fun bindsAnnouncementRepository(
        announcementRepository: OfflineFirstAnnouncementRepository,
    ): AnnouncementRepository
}