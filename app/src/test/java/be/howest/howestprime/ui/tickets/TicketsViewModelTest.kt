package be.howest.howestprime.ui.tickets

import be.howest.howestprime.domain.model.MovieReminderOption
import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.domain.usecase.GetAllTicketsUseCase
import be.howest.howestprime.domain.usecase.GetUpcomingTicketsUseCase
import be.howest.howestprime.domain.usecase.ScheduleMovieReminderUseCase
import be.howest.howestprime.fake.FakeTicketRepository
import be.howest.howestprime.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class TicketsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeTicketRepository
    private lateinit var viewModel: TicketsViewModel

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
        viewModel = TicketsViewModel(
            getAllTickets = GetAllTicketsUseCase(fakeRepository),
            getUpcomingTickets = GetUpcomingTicketsUseCase(fakeRepository),
            scheduleMovieReminder = ScheduleMovieReminderUseCase(fakeRepository)
        )
    }

    @Test
    fun initial_state_has_empty_ticket_list() {
        assertEquals(emptyList<Ticket>(), viewModel.uiState.value.tickets)
    }

    @Test
    fun initial_state_shows_all_tickets_not_upcoming_only() {
        assertFalse(viewModel.uiState.value.showUpcomingOnly)
    }

    @Test
    fun initial_state_has_all_reminder_options() {
        assertEquals(MovieReminderOption.entries, viewModel.uiState.value.reminderOptions)
    }

    @Test
    fun tickets_are_loaded_from_repository_on_init() = runTest {
        fakeRepository.setAllTickets(listOf(ticket1, ticket2))
        val newViewModel = TicketsViewModel(
            getAllTickets = GetAllTicketsUseCase(fakeRepository),
            getUpcomingTickets = GetUpcomingTicketsUseCase(fakeRepository),
            scheduleMovieReminder = ScheduleMovieReminderUseCase(fakeRepository)
        )
        assertEquals(listOf(ticket1, ticket2), newViewModel.uiState.value.tickets)
    }

    @Test
    fun toggleUpcomingOnly_switches_to_upcoming_tickets() = runTest {
        fakeRepository.setAllTickets(listOf(ticket1, ticket2))
        fakeRepository.setUpcomingTickets(listOf(ticket1))
        viewModel.toggleUpcomingOnly()
        assertEquals(listOf(ticket1), viewModel.uiState.value.tickets)
        assertTrue(viewModel.uiState.value.showUpcomingOnly)
    }

    @Test
    fun toggleUpcomingOnly_twice_returns_to_all_tickets() = runTest {
        fakeRepository.setAllTickets(listOf(ticket1, ticket2))
        fakeRepository.setUpcomingTickets(listOf(ticket1))
        viewModel.toggleUpcomingOnly()
        viewModel.toggleUpcomingOnly()
        assertEquals(listOf(ticket1, ticket2), viewModel.uiState.value.tickets)
        assertFalse(viewModel.uiState.value.showUpcomingOnly)
    }

    @Test
    fun scheduleReminder_delegates_to_repository() {
        viewModel.scheduleReminder(ticket1, 30)
        assertEquals(ticket1, fakeRepository.lastScheduledTicket)
        assertEquals(30, fakeRepository.lastScheduledMinutes)
    }

    @Test
    fun setPendingReminder_then_schedulePendingReminder_calls_repository() {
        viewModel.setPendingReminder(ticket1, 60)
        viewModel.schedulePendingReminder()
        assertEquals(ticket1, fakeRepository.lastScheduledTicket)
        assertEquals(60, fakeRepository.lastScheduledMinutes)
    }

    @Test
    fun schedulePendingReminder_clears_pending_reminder_after_scheduling() {
        viewModel.setPendingReminder(ticket1, 60)
        viewModel.schedulePendingReminder()
        viewModel.schedulePendingReminder()
        assertEquals(1, fakeRepository.scheduleReminderCallCount)
    }

    @Test
    fun schedulePendingReminder_does_nothing_when_no_pending_reminder() {
        viewModel.schedulePendingReminder()
        assertNull(fakeRepository.lastScheduledTicket)
    }

    @Test
    fun tickets_list_updates_when_repository_emits_new_data() = runTest {
        fakeRepository.setAllTickets(listOf(ticket1))
        assertEquals(1, viewModel.uiState.value.tickets.size)
        fakeRepository.setAllTickets(listOf(ticket1, ticket2))
        assertEquals(2, viewModel.uiState.value.tickets.size)
    }
}
