package io.github.helpingstar.protest_alert.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.helpingstar.protest_alert.database.PaDatabase
import io.github.helpingstar.protest_alert.database.dao.ProtestResourceDao
import io.github.helpingstar.protest_alert.database.dao.RegionDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesRegionsDao(
        database: PaDatabase,
    ): RegionDao = database.regionDao()

    @Provides
    fun providesProtestResourceDao(
        database: PaDatabase
    ): ProtestResourceDao = database.protestResourceDao()
}