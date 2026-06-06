package be.howest.howestprime.data.repository

import android.util.Log
import be.howest.howestprime.data.messaging.MessagePublisher
import be.howest.howestprime.domain.model.CartItem
import be.howest.howestprime.domain.repository.CartRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val messagePublisher: MessagePublisher,
    private val moshi: Moshi
) : CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    override suspend fun addItem(item: CartItem) {
        Log.i("CartRepository", "Adding item to cart: ${item.name}")
        _cartItems.update { current ->
            val existing = current.find { it.snackId == item.snackId }
            if (existing != null) {
                current.map { if (it.snackId == item.snackId) it.copy(quantity = it.quantity + 1) else it }
            } else {
                current + item
            }
        }
    }

    override fun getCartItems(): Flow<List<CartItem>> {
        return _cartItems.asStateFlow()
    }

    override suspend fun clearCart() {
        Log.i("CartRepository", "Clearing cart")
        _cartItems.value = emptyList()
    }

    override suspend fun placeOrder() {
        Log.i("CartRepository", "Placing order")
        val cartItems = _cartItems.value

        if (cartItems.isEmpty()) return

        try {
            val type = Types.newParameterizedType(
                List::class.java,
                CartItem::class.java
            )

            val adapter = moshi.adapter<List<CartItem>>(type)
            val json = adapter.toJson(cartItems)

            messagePublisher.publishMessage(json)
            Log.i("CartRepository", "Order published successfully")

            clearCart()
        } catch (e: Exception) {
            Log.e("CartRepository", "Failed to publish order", e)
        }
    }
}
