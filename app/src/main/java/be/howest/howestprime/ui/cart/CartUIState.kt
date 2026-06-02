package be.howest.howestprime.ui.cart

import be.howest.howestprime.domain.model.CartItem
import java.time.LocalDate

data class CartUIState(
    val items: List<CartItem> = emptyList(),
    val pickupDate: LocalDate = LocalDate.now(),
    val reminderEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isPlacingOrder: Boolean = false,
    val isOrderPlaced: Boolean = false,
    val errorMessage: String? = null
) {
    val totalItems: Int = items.sumOf { it.quantity }
    val totalPrice: Double = items.sumOf { it.price * it.quantity }
}
