package be.howest.howestprime.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val id: String,
    val movieTitle: String,
    val dateTime: LocalDateTime,
    val totalTicket: Int
)
