package be.howest.howestprime.ui.cart

import be.howest.howestprime.domain.model.CartItem
import be.howest.howestprime.domain.usecase.ClearCartUseCase
import be.howest.howestprime.domain.usecase.GetCartItemsUseCase
import be.howest.howestprime.domain.usecase.PlaceOrderUseCase
import be.howest.howestprime.fake.FakeCartRepository
import be.howest.howestprime.util.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeCartRepository
    private lateinit var viewModel: CartViewModel

    private val item1 = CartItem(snackId = "s1", name = "Popcorn", price = 4.99, quantity = 2)
    private val item2 = CartItem(snackId = "s2", name = "Cola", price = 3.49, quantity = 1)

    @Before
    fun setUp() {
        fakeRepository = FakeCartRepository()
        viewModel = CartViewModel(
            getCartItems = GetCartItemsUseCase(fakeRepository),
            makeOrder = PlaceOrderUseCase(fakeRepository),
            clearCartItems = ClearCartUseCase(fakeRepository)
        )
    }

    @Test
    fun initial_state_has_empty_cart() {
        assertTrue(viewModel.uiState.value.items.isEmpty())
    }

    @Test
    fun initial_state_is_not_placing_order() {
        assertFalse(viewModel.uiState.value.isPlacingOrder)
    }

    @Test
    fun initial_state_has_no_error() {
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun cart_items_are_reflected_in_state() = runTest {
        fakeRepository.addItem(item1)
        assertEquals(listOf(item1), viewModel.uiState.value.items)
    }

    @Test
    fun placeOrder_calls_repository() = runTest {
        fakeRepository.addItem(item1)
        viewModel.placeOrder()
        assertEquals(1, fakeRepository.placeOrderCallCount)
    }

    @Test
    fun placeOrder_sets_isOrderPlaced_to_true_on_success() = runTest {
        viewModel.placeOrder()
        assertTrue(viewModel.uiState.value.isOrderPlaced)
    }

    @Test
    fun placeOrder_sets_error_message_on_failure() = runTest {
        fakeRepository.shouldThrowOnPlaceOrder = true
        viewModel.placeOrder()
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun clearCart_calls_repository() = runTest {
        viewModel.clearCart()
        assertEquals(1, fakeRepository.clearCartCallCount)
    }

    @Test
    fun clearCart_resets_isOrderPlaced_to_false() = runTest {
        viewModel.placeOrder()
        viewModel.clearCart()
        assertFalse(viewModel.uiState.value.isOrderPlaced)
    }

    @Test
    fun clearCart_sets_error_message_on_failure() = runTest {
        fakeRepository.shouldThrowOnClearCart = true
        viewModel.clearCart()
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun totalItems_reflects_sum_of_quantities() = runTest {
        fakeRepository.addItem(item1)
        fakeRepository.addItem(item2)
        assertEquals(3, viewModel.uiState.value.totalItems)
    }

    @Test
    fun totalPrice_is_computed_correctly() = runTest {
        fakeRepository.addItem(CartItem("s1", "Popcorn", price = 4.00, quantity = 2))
        fakeRepository.addItem(CartItem("s2", "Cola", price = 3.00, quantity = 1))
        assertEquals(11.0, viewModel.uiState.value.totalPrice, 0.001)
    }
}
