package be.howest.howestprime.domain.repository

import be.howest.howestprime.domain.model.Snack

interface CartRepository {
    suspend fun addSnack(snack: Snack)
    suspend fun getCartSnacks(): List<Snack>
    suspend fun clearCart()
}
