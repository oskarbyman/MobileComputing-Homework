package fi.dy.ose.code.domain.repository

import fi.dy.ose.code.domain.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    suspend fun addReminder(reminder: Reminder): Long
    suspend fun deleteReminder(reminderId: Long)
    fun loadReminder(reminderId: Long): Reminder
    suspend fun loadReminders(): List<Reminder>
    suspend fun loadSeenReminders(seen: Boolean): List<Reminder>
    suspend fun setReminderSeen(reminderId: Long, seen: Boolean)
}