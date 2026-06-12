package be.howest.howestprime.fake

import be.howest.howestprime.domain.model.CartItem
import be.howest.howestprime.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCartRepository : CartRepository {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())

    var placeOrderCallCount = 0
    var clearCartCallCount = 0
    var shouldThrowOnPlaceOrder = false
    var shouldThrowOnClearCart = false

    override suspend fun addItem(item: CartItem) {
        val existing = _items.value.find { it.snackId == item.snackId }
        _items.value = if (existing != null) {
            _items.value.map { if (it.snackId == item.snackId) it.copy(quantity = it.quantity + 1) else it }
        } else {
            _items.value + item
        }
    }

    override fun getCartItems(): Flow<List<CartItem>> = _items.asStateFlow()

    override suspend fun clearCart() {
        if (shouldThrowOnClearCart) throw RuntimeException("Clear cart failed")
        clearCartCallCount++
        _items.value = emptyList()
    }

    override suspend fun placeOrder() {
        if (shouldThrowOnPlaceOrder) throw RuntimeException("Place order failed")
        placeOrderCallCount++
    }
}
