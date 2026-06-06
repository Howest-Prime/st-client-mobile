package be.howest.howestprime.domain.repository

import be.howest.howestprime.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addItem(item: CartItem)
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun clearCart()
    suspend fun placeOrder()
}
