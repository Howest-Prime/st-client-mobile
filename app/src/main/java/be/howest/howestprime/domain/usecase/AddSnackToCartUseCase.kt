package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.repository.CartRepository
import javax.inject.Inject

class AddSnackToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(snack: Snack) {
        cartRepository.addSnack(snack)
    }
}