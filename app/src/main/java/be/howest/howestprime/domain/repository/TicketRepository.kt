package be.howest.howestprime.domain.repository

import be.howest.howestprime.domain.model.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    fun getAllTickets(): Flow<List<Ticket>>
    fun getUpcomingTickets(): Flow<List<Ticket>>
}