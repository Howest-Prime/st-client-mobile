package be.howest.howestprime.ui.cart

import be.howest.howestprime.domain.model.CartItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CartUIStateTest {

    @Test
    fun totalItems_is_zero_when_cart_is_empty() {
        val state = CartUIState()
        assertEquals(0, state.totalItems)
    }

    @Test
    fun totalPrice_is_zero_when_cart_is_empty() {
        val state = CartUIState()
        assertEquals(0.0, state.totalPrice, 0.001)
    }

    @Test
    fun totalItems_sums_quantities_of_all_items() {
        val state = CartUIState(
            items = listOf(
                CartItem(snackId = "1", name = "Popcorn", price = 4.99, quantity = 2),
                CartItem(snackId = "2", name = "Cola", price = 3.49, quantity = 3)
            )
        )
        assertEquals(5, state.totalItems)
    }

    @Test
    fun totalPrice_sums_price_times_quantity_for_each_item() {
        val state = CartUIState(
            items = listOf(
                CartItem(snackId = "1", name = "Popcorn", price = 4.00, quantity = 2),
                CartItem(snackId = "2", name = "Cola", price = 3.00, quantity = 3)
            )
        )
        assertEquals(17.0, state.totalPrice, 0.001)
    }

    @Test
    fun totalItems_with_single_item_quantity_one() {
        val state = CartUIState(
            items = listOf(CartItem(snackId = "1", name = "Nachos", price = 5.50, quantity = 1))
        )
        assertEquals(1, state.totalItems)
    }

    @Test
    fun totalPrice_with_single_item() {
        val state = CartUIState(
            items = listOf(CartItem(snackId = "1", name = "Nachos", price = 5.50, quantity = 2))
        )
        assertEquals(11.0, state.totalPrice, 0.001)
    }

    @Test
    fun default_state_has_no_error_and_is_not_placing_order() {
        val state = CartUIState()
        assertEquals(null, state.errorMessage)
        assertEquals(false, state.isPlacingOrder)
        assertEquals(false, state.isOrderPlaced)
    }
}
