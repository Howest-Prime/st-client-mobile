package be.howest.howestprime.ui.tickets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import be.howest.howestprime.domain.model.Ticket
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class TicketsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ticketsScreen_showsEmptyMessage_whenNoTickets() {
        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(tickets = emptyList()),
                onToggleUpcomingOnly = {},
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("tickets_empty_text")
            .assertIsDisplayed()
    }

    @Test
    fun ticketsScreen_showsTicketCards_whenTicketsExist() {
        val ticket = Ticket("t1", "Inception", LocalDateTime.of(2025, 9, 1, 20, 0), 2)

        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(tickets = listOf(ticket)),
                onToggleUpcomingOnly = {},
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithText("Inception")
            .assertIsDisplayed()
    }

    @Test
    fun ticketsScreen_emptyMessage_notVisible_whenTicketsExist() {
        val ticket = Ticket("t1", "Inception", LocalDateTime.of(2025, 9, 1, 20, 0), 2)

        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(tickets = listOf(ticket)),
                onToggleUpcomingOnly = {},
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("tickets_empty_text")
            .assertDoesNotExist()
    }

    @Test
    fun ticketsScreen_upcomingFilter_isVisible() {
        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(),
                onToggleUpcomingOnly = {},
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("upcoming_filter_switch")
            .assertIsDisplayed()
    }

    @Test
    fun ticketsScreen_upcomingFilter_isOff_byDefault() {
        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(showUpcomingOnly = false),
                onToggleUpcomingOnly = {},
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("upcoming_filter_switch")
            .assertIsOff()
    }

    @Test
    fun ticketsScreen_upcomingFilter_isOn_whenEnabled() {
        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(showUpcomingOnly = true),
                onToggleUpcomingOnly = {},
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("upcoming_filter_switch")
            .assertIsOn()
    }

    @Test
    fun ticketsScreen_upcomingFilter_click_triggersCallback() {
        var toggled = false

        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(),
                onToggleUpcomingOnly = { toggled = true },
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("upcoming_filter_switch")
            .performClick()

        assertTrue(toggled)
    }

    @Test
    fun ticketsScreen_showsErrorMessage_whenErrorOccurs() {
        composeTestRule.setContent {
            TicketsScreen(
                uiState = TicketsUIState(errorMessage = "Something went wrong"),
                onToggleUpcomingOnly = {},
                onMovieReminderClick = { _, _ -> }
            )
        }

        composeTestRule
            .onNodeWithTag("tickets_error_text")
            .assertIsDisplayed()
    }
}
