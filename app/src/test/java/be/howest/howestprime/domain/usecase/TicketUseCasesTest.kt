package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.fake.FakeTicketRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class TicketUseCasesTest {

    private lateinit var fakeRepository: FakeTicketRepository

    private val ticket1 = Ticket(
        id = "t1", movieTitle = "Inception",
        dateTime = LocalDateTime.of(2026, 8, 1, 20, 0), totalTicket = 2
    )

    private val ticket2 = Ticket(
        id = "t2", movieTitle = "Dune",
        dateTime = LocalDateTime.of(2026, 9, 15, 19, 30), totalTicket = 1
    )

    @Before
    fun setUp() {
        fakeRepository = FakeTicketRepository()
    }

    @Test
    fun getAllTickets_returns_all_tickets_from_repository() = runTest {
        fakeRepository.setAllTickets(listOf(ticket1, ticket2))
        val useCase = GetAllTicketsUseCase(fakeRepository)
        val result = useCase().first()
        assertEquals(listOf(ticket1, ticket2), result)
    }

    @Test
    fun getAllTickets_returns_empty_list_when_repository_is_empty() = runTest {
        val useCase = GetAllTicketsUseCase(fakeRepository)
        val result = useCase().first()
        assertEquals(emptyList<Ticket>(), result)
    }

    @Test
    fun getUpcomingTickets_returns_upcoming_tickets_from_repository() = runTest {
        fakeRepository.setUpcomingTickets(listOf(ticket1))
        val useCase = GetUpcomingTicketsUseCase(fakeRepository)
        val result = useCase().first()
        assertEquals(listOf(ticket1), result)
    }

    @Test
    fun getUpcomingTickets_returns_empty_when_no_upcoming_tickets() = runTest {
        val useCase = GetUpcomingTicketsUseCase(fakeRepository)
        val result = useCase().first()
        assertEquals(emptyList<Ticket>(), result)
    }

    @Test
    fun getTicketById_returns_matching_ticket() = runTest {
        fakeRepository.setAllTickets(listOf(ticket1, ticket2))
        val useCase = GetTicketByIdUseCase(fakeRepository)
        val result = useCase("t1").first()
        assertEquals(ticket1, result)
    }

    @Test
    fun getTicketById_returns_null_when_ticket_not_found() = runTest {
        fakeRepository.setAllTickets(listOf(ticket1))
        val useCase = GetTicketByIdUseCase(fakeRepository)
        val result = useCase("unknown").first()
        assertNull(result)
    }

    @Test
    fun scheduleMovieReminder_delegates_to_repository() {
        val useCase = ScheduleMovieReminderUseCase(fakeRepository)
        useCase(ticket1, 30)
        assertEquals(ticket1, fakeRepository.lastScheduledTicket)
        assertEquals(30, fakeRepository.lastScheduledMinutes)
    }

    @Test
    fun scheduleMovieReminder_can_be_called_multiple_times() {
        val useCase = ScheduleMovieReminderUseCase(fakeRepository)
        useCase(ticket1, 30)
        useCase(ticket2, 60)
        assertEquals(2, fakeRepository.scheduleReminderCallCount)
        assertEquals(ticket2, fakeRepository.lastScheduledTicket)
    }
}
