package be.howest.howestprime.domain.repository

import be.howest.howestprime.domain.model.Ticket

interface TicketRepository {
    suspend fun getAllTickets(): List<Ticket>
    suspend fun getUpcomingTickets(): List<Ticket>

}