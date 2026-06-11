package be.howest.howestprime.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey val id: String,
    val movieTitle: String,
    val dateTime: LocalDateTime,
    val totalTicket: Int,
    val posterUrl: String? = null,
    val actors: String? = null,
    val description: String? = null,
    val duration: Int? = null,
    val genre: String? = null,
    val releaseDate: LocalDate? = null,
    val director: String? = null
)
