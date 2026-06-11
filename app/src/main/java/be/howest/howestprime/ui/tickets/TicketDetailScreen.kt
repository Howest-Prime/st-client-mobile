package be.howest.howestprime.ui.tickets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.howest.howestprime.R
import be.howest.howestprime.domain.model.Ticket
import be.howest.howestprime.ui.theme.HowestMain
import java.time.format.DateTimeFormatter

@Composable
fun TicketDetailScreen(
    uiState: TicketDetailUIState,
    onSetReminder: (Ticket, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.errorMessage != null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
        return
    }

    val ticket = uiState.ticket ?: return

    var showReminderPopUp by remember { mutableStateOf(false) }

    if (showReminderPopUp) {
        ReminderPopUp(
            ticket = ticket,
            options = uiState.reminderOptions,
            onConfirm = { minutesBefore -> onSetReminder(ticket, minutesBefore) },
            onDismiss = { showReminderPopUp = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = ticket.movieTitle,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        InfoRow(
            icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = HowestMain) },
            text = ticket.dateTime.format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy  •  HH:mm"))
        )

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(
            icon = { Icon(Icons.Outlined.ConfirmationNumber, contentDescription = null, tint = HowestMain) },
            text = "${ticket.totalTicket} ${stringResource(R.string.ticket_detail_tickets_label)}"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        Text(
            text = stringResource(R.string.qr_popup_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        TicketQrCode(ticketId = ticket.id, size = 260.dp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { showReminderPopUp = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = HowestMain)
        ) {
            Icon(
                Icons.Outlined.Alarm,
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 4.dp)
            )
            Text(stringResource(R.string.ticket_detail_set_reminder))
        }
    }
}

@Composable
private fun InfoRow(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        icon()
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
