package fi.dy.ose.core_data.repository

import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.core_data.datasource.reminder.ReminderDataSource

class ReminderRepositoryImpl (
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDataSource.addReminder(reminder)
    }
}