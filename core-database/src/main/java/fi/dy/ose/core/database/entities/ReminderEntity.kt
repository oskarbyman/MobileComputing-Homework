package fi.dy.ose.core.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "reminders",
    indices = [
        Index("reminderId", unique = true)
    ]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0,
    val title: String,
    val message: String,
    val location_x: Float?,
    val location_y: Float?,
    val reminder_time: LocalDateTime,
    val creation_time: LocalDateTime,
    val creator_id: String,
    val reminder_seen: Boolean
)
