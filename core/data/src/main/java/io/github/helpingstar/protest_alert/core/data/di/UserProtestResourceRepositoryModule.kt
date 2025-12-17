package io.github.helpingstar.protest_alert.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.helpingstar.protest_alert.core.data.repository.CompositeUserProtestResourceRepository
import io.github.helpingstar.protest_alert.core.data.repository.UserProtestResourceRepository

@Module
@InstallIn(SingletonComponent::class)
interface UserProtestResourceRepositoryModule {
    @Binds
    fun bindsUserProtestResourceRepository(
        userDataRepository: CompositeUserProtestResourceRepository
    ): UserProtestResourceRepository
}