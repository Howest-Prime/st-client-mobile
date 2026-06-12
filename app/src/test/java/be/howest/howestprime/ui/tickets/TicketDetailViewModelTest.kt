package be.howest.howestprime.ui.tickets

import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.usecase.GetTicketByIdUseCase
import be.howest.howestprime.domain.usecase.ScheduleMovieReminderUseCase
import be.howest.howestprime.fake.FakeTicketRepository
import be.howest.howestprime.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class TicketDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeTicketRepository
    private lateinit var viewModel: TicketDetailViewModel

    private val ticket = Ticket(
        id = "t1", movieTitle = "Inception",
        dateTime = LocalDateTime.of(2026, 8, 1, 20, 0), totalTicket = 2
    )

    @Before
    fun setUp() {
        fakeRepository = FakeTicketRepository()
        viewModel = TicketDetailViewModel(
            getTicketById = GetTicketByIdUseCase(fakeRepository),
            scheduleMovieReminder = ScheduleMovieReminderUseCase(fakeRepository)
        )
    }

    @Test
    fun initial_state_has_no_ticket() {
        assertNull(viewModel.uiState.value.ticket)
    }

    @Test
    fun initial_state_has_no_error() {
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun loadTicket_updates_state_with_found_ticket() = runTest {
        fakeRepository.setAllTickets(listOf(ticket))
        viewModel.loadTicket("t1")
        assertEquals(ticket, viewModel.uiState.value.ticket)
    }

    @Test
    fun loadTicket_sets_null_ticket_when_not_found() = runTest {
        fakeRepository.setAllTickets(emptyList())
        viewModel.loadTicket("unknown")
        assertNull(viewModel.uiState.value.ticket)
    }

    @Test
    fun loadTicket_clears_error_message_when_new_ticket_has_different_id() = runTest {
        val ticket2 = Ticket(
            id = "t2", movieTitle = "Dune",
            dateTime = LocalDateTime.of(2026, 9, 1, 19, 0), totalTicket = 1
        )
        fakeRepository.setAllTickets(listOf(ticket))
        viewModel.loadTicket("t1")
        fakeRepository.setAllTickets(listOf(ticket2))
        viewModel.loadTicket("t2")
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun scheduleReminder_delegates_to_repository() {
        viewModel.scheduleReminder(ticket, 30)
        assertEquals(ticket, fakeRepository.lastScheduledTicket)
        assertEquals(30, fakeRepository.lastScheduledMinutes)
    }

    @Test
    fun setPendingReminder_then_schedulePendingReminder_calls_repository() {
        viewModel.setPendingReminder(ticket, 60)
        viewModel.schedulePendingReminder()
        assertEquals(ticket, fakeRepository.lastScheduledTicket)
        assertEquals(60, fakeRepository.lastScheduledMinutes)
    }

    @Test
    fun schedulePendingReminder_does_nothing_without_pending_reminder() {
        viewModel.schedulePendingReminder()
        assertNull(fakeRepository.lastScheduledTicket)
    }

    @Test
    fun schedulePendingReminder_clears_pending_after_first_call() {
        viewModel.setPendingReminder(ticket, 30)
        viewModel.schedulePendingReminder()
        viewModel.schedulePendingReminder()
        assertEquals(1, fakeRepository.scheduleReminderCallCount)
    }

    @Test
    fun ticket_state_updates_when_repository_emits_new_data() = runTest {
        fakeRepository.setAllTickets(listOf(ticket))
        viewModel.loadTicket("t1")
        assertNotNull(viewModel.uiState.value.ticket)
        fakeRepository.setAllTickets(emptyList())
        assertNull(viewModel.uiState.value.ticket)
    }
}
