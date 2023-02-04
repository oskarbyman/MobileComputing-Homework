package fi.dy.ose.core_data.datasource.reminder

import fi.dy.ose.code.domain.entity.Reminder

interface ReminderDataSource {
    suspend fun addReminder(reminder: Reminder)
}