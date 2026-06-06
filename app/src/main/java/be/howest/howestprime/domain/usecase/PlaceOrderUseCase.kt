package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.CartItem
import be.howest.howestprime.domain.repository.CartRepository
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(items: List<CartItem>) {
        cartRepository.placeOrder()
    }
}
