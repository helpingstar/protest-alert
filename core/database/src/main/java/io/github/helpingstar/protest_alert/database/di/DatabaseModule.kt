package io.github.helpingstar.protest_alert.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.helpingstar.protest_alert.database.PaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesPaDatabase(
        @ApplicationContext context: Context,
    ): PaDatabase = Room.databaseBuilder(
        context,
        PaDatabase::class.java,
        "pa-database"
    ).build()
}