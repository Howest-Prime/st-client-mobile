package be.howest.howestprime.domain.model

import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Ticket(
    val id: String,
    val movieTitle: String,
    val dateTime: LocalDateTime,
    val totalTicket: Int,
    val posterUrl: String? = null,
    val actors: List<String> = emptyList(),
    val description: String? = null,
    val duration: Int? = null,
    val genre: List<String> = emptyList(),
    val releaseDate: LocalDate? = null,
    val director: String? = null
)