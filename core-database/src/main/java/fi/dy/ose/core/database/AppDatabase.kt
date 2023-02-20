package fi.dy.ose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fi.dy.ose.core.database.dao.ReminderDao
import fi.dy.ose.core.database.entities.ReminderEntity
import fi.dy.ose.core.database.utils.LocalDateTimeConverter

@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}