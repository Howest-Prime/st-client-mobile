package be.howest.howestprime.data.repository

import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import be.howest.howestprime.data.local.room.dao.TicketDao
import be.howest.howestprime.data.local.room.entity.TicketEntity
import be.howest.howestprime.data.messaging.MessageConsumer
import be.howest.howestprime.data.worker.NotficationWorker
import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.repository.TicketRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepositoryImpl @Inject constructor(
    private val ticketDao: TicketDao,
    private val consumer: MessageConsumer,
    private val moshi: Moshi,
    private val workManager: WorkManager
) : TicketRepository {

    private val ticketAdapter = moshi.adapter(Ticket::class.java)

    init {
        consumer.onMessageReceived = { json ->
            Log.d("TicketRepository", "Received message: $json")
            val ticket = ticketAdapter.fromJson(json)
            if (ticket != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    ticketDao.insertTicket(
                        TicketEntity(
                            id = ticket.id,
                            movieTitle = ticket.movieTitle,
                            dateTime = ticket.dateTime,
                            totalTicket = ticket.totalTicket,
                            posterUrl = ticket.posterUrl,
                            actors = ticket.actors.joinToString(","),
                            description = ticket.description,
                            duration = ticket.duration,
                            genre = ticket.genre.joinToString(","),
                            releaseDate = ticket.releaseDate,
                            director = ticket.director
                        )
                    )
                }
            }
        }

        consumer.startConsuming()
    }

    override fun getAllTickets(): Flow<List<Ticket>> {
        Log.i("TicketRepository", "Fetching all tickets")
        return ticketDao.getAllTickets().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUpcomingTickets(): Flow<List<Ticket>> {
        val now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        Log.i("TicketRepository", "Fetching upcoming tickets from $now")
        return ticketDao.getUpcomingTickets(now).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTicketById(id: String): Flow<Ticket?> {
        return ticketDao.getTicketById(id).map { it?.toDomain() }
    }

    override fun scheduleReminder(ticket: Ticket, minutesBefore: Int) {
        val reminderTime = ticket.dateTime.minusMinutes(minutesBefore.toLong())
        val delayInMs = ChronoUnit.MILLIS.between(LocalDateTime.now(), reminderTime)
        if (delayInMs <= 0) return

        val inputData = workDataOf(
            "TICKET_ID" to ticket.id,
            "MOVIE_NAME" to ticket.movieTitle,
            "DATE" to ticket.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            "TIME" to ticket.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        )

        val workRequest = OneTimeWorkRequestBuilder<NotficationWorker>()
            .setInitialDelay(delayInMs, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        workManager.enqueue(workRequest)
    }
}

private fun TicketEntity.toDomain() = Ticket(
    id = id,
    movieTitle = movieTitle,
    dateTime = dateTime,
    totalTicket = totalTicket,
    posterUrl = posterUrl,
    actors = actors?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList(),
    description = description,
    duration = duration,
    genre = genre?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList(),
    releaseDate = releaseDate,
    director = director
)
