package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.SubscriptionRepository
import javax.inject.Inject

class SubscribeToNewsletterUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(email: String) {
        subscriptionRepository.subscribe(email)
    }
}
