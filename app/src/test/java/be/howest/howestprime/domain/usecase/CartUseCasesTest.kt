package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.CartItem
import be.howest.howestprime.fake.FakeCartRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartUseCasesTest {

    private lateinit var fakeRepository: FakeCartRepository

    private val cartItem = CartItem(snackId = "s1", name = "Popcorn", price = 4.99, quantity = 1)

    @Before
    fun setUp() {
        fakeRepository = FakeCartRepository()
    }

    @Test
    fun getCartItems_returns_items_from_repository() = runTest {
        fakeRepository.addItem(cartItem)
        val useCase = GetCartItemsUseCase(fakeRepository)
        val result = useCase().first()
        assertEquals(listOf(cartItem), result)
    }

    @Test
    fun getCartItems_returns_empty_list_initially() = runTest {
        val useCase = GetCartItemsUseCase(fakeRepository)
        val result = useCase().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun placeOrder_delegates_to_repository() = runTest {
        val useCase = PlaceOrderUseCase(fakeRepository)
        useCase()
        assertEquals(1, fakeRepository.placeOrderCallCount)
    }

    @Test
    fun placeOrder_can_be_called_multiple_times() = runTest {
        val useCase = PlaceOrderUseCase(fakeRepository)
        useCase()
        useCase()
        assertEquals(2, fakeRepository.placeOrderCallCount)
    }

    @Test
    fun clearCart_delegates_to_repository() = runTest {
        val useCase = ClearCartUseCase(fakeRepository)
        useCase()
        assertEquals(1, fakeRepository.clearCartCallCount)
    }

    @Test
    fun clearCart_empties_the_cart() = runTest {
        fakeRepository.addItem(cartItem)
        val useCase = ClearCartUseCase(fakeRepository)
        useCase()
        val items = fakeRepository.getCartItems().first()
        assertTrue(items.isEmpty())
    }
}
