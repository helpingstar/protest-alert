package io.github.helpingstar.protest_alert.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.helpingstar.protest_alert.database.dao.ProtestResourceDao
import io.github.helpingstar.protest_alert.database.dao.RegionDao
import io.github.helpingstar.protest_alert.database.model.ProtestResourceEntity
import io.github.helpingstar.protest_alert.database.model.RegionEntity
import io.github.helpingstar.protest_alert.database.util.InstantConverter
import io.github.helpingstar.protest_alert.database.util.JsonObjectConverter
import io.github.helpingstar.protest_alert.database.util.LocalDateConverter

@Database(
    entities = [
        ProtestResourceEntity::class,
        RegionEntity::class
    ],
    version = 1,
)
@TypeConverters(
    InstantConverter::class,
    JsonObjectConverter::class,
    LocalDateConverter::class
)
internal abstract class PaDatabase : RoomDatabase() {
    abstract fun regionDao(): RegionDao
    abstract fun protestResourceDao(): ProtestResourceDao
}