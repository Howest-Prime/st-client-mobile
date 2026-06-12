package be.howest.howestprime.ui.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.howest.howestprime.R
import be.howest.howestprime.domain.model.MovieReminderOption
import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.ui.theme.HowestMain
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TicketsScreen(
    uiState: TicketsUIState,
    onToggleUpcomingOnly: () -> Unit,
    onMovieReminderClick: (ticket: Ticket, minutesBefore: Int) -> Unit,
    onTicketClick: (Ticket) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var qrTicket by remember { mutableStateOf<Ticket?>(null) }
    var alarmTicket by remember { mutableStateOf<Ticket?>(null) }

    qrTicket?.let { 
        QrDialog(ticket = it, 
        onDismiss = { qrTicket = null }) 
        }

    alarmTicket?.let { ticket ->
        ReminderPopUp(
            ticket = ticket,
            options = uiState.reminderOptions,
            onConfirm = { minutesBefore -> onMovieReminderClick(ticket, minutesBefore) },
            onDismiss = { alarmTicket = null }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("tickets_screen")
    ) {
        TicketsHeader()

        UpcomingFilter(
            checked = uiState.showUpcomingOnly,
            onCheckedChange = { onToggleUpcomingOnly() }
        )

        TicketsContent(
            uiState = uiState,
            onQrClick = { qrTicket = it },
            onAlarmClick = { alarmTicket = it },
            onTicketClick = onTicketClick
        )
    }
}

@Composable
private fun TicketsHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
) {
        Text(
            text = stringResource(R.string.tickets_title),
            modifier = Modifier.padding(vertical = 24.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun UpcomingFilter(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.show_upcoming_only),
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier.testTag("upcoming_filter_switch"))
    }
}

@Composable
private fun TicketsContent(
    uiState: TicketsUIState,
    onQrClick: (Ticket) -> Unit,
    onAlarmClick: (Ticket) -> Unit,
    onTicketClick: (Ticket) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.errorMessage != null -> {
            Text(
                text = uiState.errorMessage,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .testTag("tickets_error_text"),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
        uiState.tickets.isEmpty() -> {
            Text(
                text = stringResource(R.string.tickets_empty),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .testTag("tickets_empty_text"),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        else -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.tickets.forEach { ticket ->
                    TicketCard(
                        ticket = ticket,
                        onQrClick = { onQrClick(ticket) },
                        onAlarmClick = { onAlarmClick(ticket) },
                        onClick = { onTicketClick(ticket) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TicketCard(
    ticket: Ticket,
    onQrClick: () -> Unit,
    onAlarmClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(ticket.movieTitle, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(
                    ticket.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "${stringResource(R.string.tickets_count_label)} ${ticket.totalTicket}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row {
                IconButton(onClick = onQrClick) {
                    Icon(
                        Icons.Outlined.QrCode,
                        contentDescription = stringResource(R.string.qr_code_description),
                        tint = HowestMain
                    )
                }
                IconButton(onClick = onAlarmClick) {
                    Icon(
                        Icons.Outlined.Alarm,
                        contentDescription = stringResource(R.string.set_alarm_description),
                        tint = HowestMain
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketsScreenPreview() {
    TicketsScreen(
        uiState = TicketsUIState(
            tickets = listOf(
                Ticket("test1", "Inception", LocalDateTime.of(2025, 7, 12, 20, 30), 2),
                Ticket("Test2", "Interstellar", LocalDateTime.of(2025, 7, 20, 18, 0), 3),
                Ticket("Test3", "The Dark Knight", LocalDateTime.of(2025, 8, 5, 21, 15), 1)
            )
        ),
        onToggleUpcomingOnly = {},
        onMovieReminderClick = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
private fun TicketsScreenEmptyPreview() {
    TicketsScreen(
        uiState = TicketsUIState(tickets = emptyList()),
        onToggleUpcomingOnly = {},
        onMovieReminderClick = { _, _ -> }
    )
}
