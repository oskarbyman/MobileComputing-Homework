package fi.dy.ose.core_data.datasource.reminder

import fi.dy.ose.code.domain.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    suspend fun addReminder(reminder: Reminder)
    suspend fun deleteReminder(reminderId: Long)
    fun loadReminder(reminderId: Long): Reminder
    suspend fun loadReminders(): List<Reminder>
}