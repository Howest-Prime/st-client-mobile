package be.howest.howestprime.data.repository

import android.util.Log
import be.howest.howestprime.data.local.room.dao.TicketDao
import be.howest.howestprime.data.local.room.entity.TicketEntity
import be.howest.howestprime.data.messaging.MessageConsumer
import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.repository.TicketRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepositoryImpl @Inject constructor(
    private val ticketDao: TicketDao,
    private val consumer: MessageConsumer,
    private val moshi: Moshi,
) : TicketRepository {

    private val ticketAdapter = moshi.adapter(Ticket::class.java)

    init {
        consumer.onMessageReceived = { json ->
            Log.d("TicketRepository", "Received message: $json")
            val ticket = ticketAdapter.fromJson(json)
            if (ticket != null) {
                ticketDao.insertTicket(
                    TicketEntity(
                        id = ticket.id,
                        movieTitle = ticket.movieTitle,
                        dateTime = ticket.dateTime,
                        totalTicket = ticket.totalTicket
                    )
                )
            }
        }

        consumer.startConsuming()
    }

    override fun getAllTickets(): Flow<List<Ticket>> {
        Log.i("TicketRepository", "Fetching all tickets")
        return ticketDao.getAllTickets().map { entities ->
            entities.map { entity ->
                Ticket(
                    id = entity.id,
                    movieTitle = entity.movieTitle,
                    dateTime = entity.dateTime,
                    totalTicket = entity.totalTicket
                )
            }
        }
    }

    override fun getUpcomingTickets(): Flow<List<Ticket>> {
        Log.i("TicketRepository", "Fetching upcoming tickets from ${LocalDateTime.now()}")
        return ticketDao.getUpcomingTickets().map { entities ->
            entities.map { entity ->
                Ticket(
                    id = entity.id,
                    movieTitle = entity.movieTitle,
                    dateTime = entity.dateTime,
                    totalTicket = entity.totalTicket
                )
            }
        }
    }
}
