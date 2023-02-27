package fi.dy.ose.core.database.dao

import androidx.room.*
import fi.dy.ose.core.database.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(reminder: ReminderEntity): Long

    @Query("SELECT * FROM reminders WHERE reminderId LIKE :reminderId")
    fun findOne(reminderId: Long):ReminderEntity

    @Query("SELECT * FROM reminders")
    suspend fun loadAll(): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE reminder_seen LIKE :seen")
    suspend fun loadSeenReminders(seen: Boolean): List<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: ReminderEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: ReminderEntity)

    @Delete
    suspend fun delete(reminder: ReminderEntity)

}