package io.github.helpingstar.protest_alert.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    )
        .addMigrations(MIGRATION_1_2)
        .build()
}

/**
 * Migration from version 1 to 2: Add announcements table.
 */
private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `announcements` (
                `id` TEXT NOT NULL PRIMARY KEY,
                `type` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `body` TEXT NOT NULL,
                `start_at` INTEGER NOT NULL,
                `end_at` INTEGER,
                `updated_at` INTEGER NOT NULL,
                `is_read` INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }
}