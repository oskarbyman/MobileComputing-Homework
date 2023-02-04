package fi.dy.ose.code.domain.entity

import java.time.LocalDateTime

data class Reminder(
    val reminderId: Long = 0,
    val title: String,
    val categoryId: Long,
    val note: String,
    val date: LocalDateTime
)
