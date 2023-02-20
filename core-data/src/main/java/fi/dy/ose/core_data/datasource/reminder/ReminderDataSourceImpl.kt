package fi.dy.ose.core_data.datasource.reminder

import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.core.database.dao.ReminderDao
import fi.dy.ose.core.database.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class ReminderDataSourceImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderDataSource {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    override suspend fun deleteReminder(reminderId: Long) {
        val reminder = reminderDao.findOne(reminderId)
        reminderDao.delete(reminder)
    }

    override fun loadReminder(reminderId: Long): Reminder {
        return reminderDao.findOne(reminderId).fromEntity()
    }

    override suspend fun loadReminders(): List<Reminder> {
        return reminderDao.loadAll()
            .map {
                it.fromEntity()
            }
    }

    private fun Reminder.toEntity() = ReminderEntity(
        reminderId = this.reminderId,
        title = this.title,
        message = this.message,
        location_x =  this.location_x,
        location_y = this.location_y,
        reminder_time = this.reminder_time,
        creation_time = this.creation_time,
        creator_id = this.creator_id,
        reminder_seen = this.reminder_seen
    )
    private fun ReminderEntity.fromEntity() = Reminder(
        reminderId = this.reminderId,
        title = this.title,
        message = this.message,
        location_x =  this.location_x,
        location_y = this.location_y,
        reminder_time = this.reminder_time,
        creation_time = this.creation_time,
        creator_id = this.creator_id,
        reminder_seen = this.reminder_seen
    )
}