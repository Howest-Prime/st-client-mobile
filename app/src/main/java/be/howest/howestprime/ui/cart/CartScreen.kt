package be.howest.howestprime.ui.cart

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.howest.howestprime.R
import be.howest.howestprime.domain.model.CartItem
import be.howest.howestprime.ui.theme.HowestMain

fun shareOrder(context: Context, items: List<CartItem>, totalPrice: Double) {
    val lines = items.joinToString("\n") { "- ${it.quantity}x ${it.name}" }
    val text = "Check out my snack order at Howest Prime!\n\n$lines\n\nTotal: €${"%.2f".format(totalPrice)}"
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "My Howest Prime Order")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share order"))
}

@Composable
fun CartScreen(
    uiState: CartUIState,
    onPlaceOrder: () -> Unit,
    onClearCart: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CartHeader(onShare = onShare, hasItems = uiState.items.isNotEmpty())
        CartContent(
            uiState = uiState,
            onPlaceOrder = onPlaceOrder,
            onClearCart = onClearCart
        )
    }
}

@Composable
private fun CartHeader(onShare: () -> Unit, hasItems: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.cart_title),
            modifier = Modifier.padding(vertical = 24.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
        if (hasItems) {
            androidx.compose.material3.IconButton(
                onClick = onShare,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Outlined.Share, contentDescription = "Share order", tint = HowestMain)
            }
        }
    }
}

@Composable
private fun CartContent(
    uiState: CartUIState,
    onPlaceOrder: () -> Unit,
    onClearCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.errorMessage != null -> {
            Text(
                text = uiState.errorMessage,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
        uiState.isOrderPlaced -> {
            Text(
                text = stringResource(R.string.cart_order_placed),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = HowestMain,
                textAlign = TextAlign.Center
            )
        }
        uiState.items.isEmpty() -> {
            Text(
                text = stringResource(R.string.cart_empty),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        else -> {
            Column(modifier = modifier.fillMaxWidth()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.items) { item ->
                        CartItemRow(item = item)
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                CartSummary(
                    totalItems = uiState.totalItems,
                    totalPrice = uiState.totalPrice,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onPlaceOrder,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isPlacingOrder,
                        colors = ButtonDefaults.buttonColors(containerColor = HowestMain)
                    ) {
                        Text(stringResource(R.string.cart_place_order))
                    }
                    OutlinedButton(
                        onClick = onClearCart,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cart_cancel))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${item.quantity} x ${item.name}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(R.string.snack_price_prefix) + "%.2f".format(item.price * item.quantity),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CartSummary(
    totalItems: Int,
    totalPrice: Double,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = stringResource(R.string.cart_total_items_prefix) + totalItems,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.cart_total_price_prefix) + "%.2f".format(totalPrice),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
