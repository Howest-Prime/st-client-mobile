package be.howest.howestprime.domain.model

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true) // generates adapter automatically domain <-> json traslator
data class Ticket(
    val id: String,
    val movieTitle: String,
    val dateTime: LocalDateTime,
    val totalTicket: Int
)