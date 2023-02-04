package fi.dy.ose.code.domain.repository

import fi.dy.ose.code.domain.entity.Reminder

interface ReminderRepository {
    suspend fun addReminder(reminder: Reminder)
}