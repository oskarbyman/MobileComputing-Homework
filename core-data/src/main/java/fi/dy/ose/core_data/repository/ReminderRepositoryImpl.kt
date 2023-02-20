package fi.dy.ose.core_data.repository

import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.code.domain.repository.ReminderRepository
import fi.dy.ose.core_data.datasource.reminder.ReminderDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {
    override suspend fun addReminder(reminder: Reminder) {
        reminderDataSource.addReminder(reminder)
    }

    override suspend fun deleteReminder(reminderId: Long) {
        reminderDataSource.deleteReminder(reminderId)
    }

    override fun loadReminder(reminderId: Long): Reminder {
        return reminderDataSource.loadReminder(reminderId)
    }

    override suspend fun loadReminders(): List<Reminder> {
        return reminderDataSource.loadReminders()
    }
}