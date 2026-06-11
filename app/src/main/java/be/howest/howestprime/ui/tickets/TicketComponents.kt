package be.howest.howestprime.ui.tickets

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import be.howest.howestprime.R
import be.howest.howestprime.domain.model.MovieReminderOption
import be.howest.howestprime.domain.model.Ticket
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import java.time.format.DateTimeFormatter

@Composable
fun TicketQrCode(ticketId: String, size: Dp = 220.dp, modifier: Modifier = Modifier) {
    val painter = rememberQrCodePainter(
        data = ticketId,
        shapes = QrShapes(
            ball = QrBallShape.Default,
            darkPixel = QrPixelShape.Default,
            frame = QrFrameShape.Default
        )
    )
    Image(
        painter = painter,
        contentDescription = stringResource(R.string.qr_code_description),
        modifier = modifier.size(size)
    )
}

@Composable
fun QrDialog(ticket: Ticket, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.qr_dialog_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                TicketQrCode(ticketId = ticket.id)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = ticket.movieTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = ticket.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.qr_popup_close))
            }
        }
    )
}

@Composable
fun ReminderPopUp(
    ticket: Ticket,
    options: List<MovieReminderOption>,
    onConfirm: (minutesBefore: Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.alarm_popup_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = ticket.movieTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = ticket.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.alarm_popup_message),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                options.forEach { option ->
                    TextButton(
                        onClick = { onConfirm(option.minutesBefore); onDismiss() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(option.labelRes()))
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.alarm_popup_cancel))
            }
        }
    )
}

@StringRes
fun MovieReminderOption.labelRes(): Int = when (this) {
    MovieReminderOption.TWO_MINUTES -> R.string.alarm_option_2min
    MovieReminderOption.THIRTY_MINUTES -> R.string.alarm_option_30min
    MovieReminderOption.ONE_HOUR -> R.string.alarm_option_1hr
    MovieReminderOption.TWO_HOURS -> R.string.alarm_option_2hr
    MovieReminderOption.ONE_DAY -> R.string.alarm_option_1day
}
