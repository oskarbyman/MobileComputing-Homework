package fi.dy.ose.core_data.datasource.reminder

import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.core.database.dao.ReminderDao
import fi.dy.ose.core.database.entities.ReminderEntity

class ReminderDataSourceImpl(
    private val reminderDao: ReminderDao
) : ReminderDataSource {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDao.insertOrUpdate(reminder.toEntity())
    }

    private fun Reminder.toEntity() = ReminderEntity(
        reminderId = this.reminderId,
        categoryId = this.categoryId,
        title = this.title,
        note = this.note,
        date = this.date
    )
    private fun ReminderEntity.fromEntity() = Reminder(
        reminderId = this.reminderId,
        categoryId = this.categoryId,
        title = this.title,
        note = this.note,
        date = this.date
    )
}