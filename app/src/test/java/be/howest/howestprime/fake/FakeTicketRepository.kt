package be.howest.howestprime.fake

import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.repository.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeTicketRepository : TicketRepository {

    private val _allTickets = MutableStateFlow<List<Ticket>>(emptyList())
    private val _upcomingTickets = MutableStateFlow<List<Ticket>>(emptyList())

    var lastScheduledTicket: Ticket? = null
    var lastScheduledMinutes: Int? = null
    var scheduleReminderCallCount = 0

    fun setAllTickets(tickets: List<Ticket>) {
        _allTickets.value = tickets
    }

    fun setUpcomingTickets(tickets: List<Ticket>) {
        _upcomingTickets.value = tickets
    }

    override fun getAllTickets(): Flow<List<Ticket>> = _allTickets.asStateFlow()

    override fun getUpcomingTickets(): Flow<List<Ticket>> = _upcomingTickets.asStateFlow()

    override fun getTicketById(id: String): Flow<Ticket?> =
        _allTickets.map { tickets -> tickets.firstOrNull { it.id == id } }

    override fun scheduleReminder(ticket: Ticket, minutesBefore: Int) {
        lastScheduledTicket = ticket
        lastScheduledMinutes = minutesBefore
        scheduleReminderCallCount++
    }
}
