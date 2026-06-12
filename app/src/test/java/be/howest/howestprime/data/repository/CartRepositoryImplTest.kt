package be.howest.howestprime.data.repository

import be.howest.howestprime.domain.model.CartItem
import be.howest.howestprime.fake.FakeMessagePublisher
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {

    private lateinit var fakePublisher: FakeMessagePublisher
    private lateinit var repository: CartRepositoryImpl
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        fakePublisher = FakeMessagePublisher()
        moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        repository = CartRepositoryImpl(fakePublisher, moshi)
    }

    @Test
    fun initial_cart_is_empty() = runTest {
        val items = repository.getCartItems().first()
        assertTrue(items.isEmpty())
    }

    @Test
    fun addItem_adds_new_item_to_cart() = runTest {
        val item = CartItem(snackId = "1", name = "Popcorn", price = 4.99, quantity = 1)
        repository.addItem(item)
        val items = repository.getCartItems().first()
        assertEquals(1, items.size)
        assertEquals(item, items.first())
    }

    @Test
    fun addItem_increments_quantity_for_existing_snack() = runTest {
        val item = CartItem(snackId = "1", name = "Popcorn", price = 4.99, quantity = 1)
        repository.addItem(item)
        repository.addItem(item)
        val items = repository.getCartItems().first()
        assertEquals(1, items.size)
        assertEquals(2, items.first().quantity)
    }

    @Test
    fun addItem_keeps_different_snacks_separate() = runTest {
        repository.addItem(CartItem(snackId = "1", name = "Popcorn", price = 4.99, quantity = 1))
        repository.addItem(CartItem(snackId = "2", name = "Cola", price = 3.49, quantity = 1))
        val items = repository.getCartItems().first()
        assertEquals(2, items.size)
    }

    @Test
    fun clearCart_empties_the_cart() = runTest {
        repository.addItem(CartItem(snackId = "1", name = "Popcorn", price = 4.99, quantity = 1))
        repository.clearCart()
        val items = repository.getCartItems().first()
        assertTrue(items.isEmpty())
    }

    @Test
    fun placeOrder_publishes_cart_as_json_and_clears_cart() = runTest {
        repository.addItem(CartItem(snackId = "1", name = "Popcorn", price = 4.00, quantity = 2))
        repository.placeOrder()
        assertEquals(1, fakePublisher.publishCallCount)
        val items = repository.getCartItems().first()
        assertTrue(items.isEmpty())
    }

    @Test
    fun placeOrder_does_nothing_when_cart_is_empty() = runTest {
        repository.placeOrder()
        assertEquals(0, fakePublisher.publishCallCount)
    }

    @Test
    fun placeOrder_published_json_contains_item_name() = runTest {
        repository.addItem(CartItem(snackId = "1", name = "Nachos", price = 5.50, quantity = 1))
        repository.placeOrder()
        assertTrue(fakePublisher.lastPublishedMessage?.contains("Nachos") == true)
    }

    @Test
    fun addItem_then_clear_then_add_again_has_single_item() = runTest {
        repository.addItem(CartItem(snackId = "1", name = "Popcorn", price = 4.99, quantity = 1))
        repository.clearCart()
        repository.addItem(CartItem(snackId = "2", name = "Cola", price = 3.49, quantity = 1))
        val items = repository.getCartItems().first()
        assertEquals(1, items.size)
        assertEquals("2", items.first().snackId)
    }

    @Test
    fun placeOrder_when_publisher_throws_cart_is_not_cleared() = runTest {
        fakePublisher.shouldThrow = true
        repository.addItem(CartItem(snackId = "1", name = "Popcorn", price = 4.99, quantity = 1))
        repository.placeOrder()
        val items = repository.getCartItems().first()
        assertEquals(1, items.size)
    }
}
