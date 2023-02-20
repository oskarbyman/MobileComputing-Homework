package fi.dy.ose.code.domain.entity

import java.time.LocalDateTime

data class Reminder(
    var reminderId: Long = 0,
    val title: String,
    val message: String,
    val location_x: Float = 0.0F,
    val location_y: Float = 0.0F,
    val reminder_time: LocalDateTime,
    val creation_time: LocalDateTime = LocalDateTime.now(),
    val creator_id: String,
    val reminder_seen: Boolean = false
)
