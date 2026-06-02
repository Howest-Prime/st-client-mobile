package be.howest.howestprime.domain.usecase

import be.howest.howestprime.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubscriptionStatusUseCase @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return subscriptionRepository.getSubscriptionStatus()
    }
}
