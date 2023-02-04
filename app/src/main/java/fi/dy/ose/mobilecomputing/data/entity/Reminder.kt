package fi.dy.ose.mobilecomputing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "reminders")
data class Reminder (
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "categoryId") val categoryId : Long,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "note") val note : String,
    @ColumnInfo(name = "date") val date : LocalDateTime
)