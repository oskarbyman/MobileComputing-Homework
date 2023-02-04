package fi.dy.ose.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fi.dy.ose.core.database.dao.ReminderDao
import fi.dy.ose.core.database.entities.ReminderEntity

@Database(
    entities = [ReminderEntity::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}