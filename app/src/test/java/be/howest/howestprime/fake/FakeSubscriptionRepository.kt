package be.howest.howestprime.fake

import be.howest.howestprime.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSubscriptionRepository : SubscriptionRepository {

    private val _isSubscribed = MutableStateFlow(false)
    var lastSubscribedEmail: String? = null
    var subscribeCallCount = 0

    fun setSubscribed(subscribed: Boolean) {
        _isSubscribed.value = subscribed
    }

    override fun getSubscriptionStatus(): Flow<Boolean> = _isSubscribed.asStateFlow()

    override suspend fun subscribe(email: String) {
        lastSubscribedEmail = email
        subscribeCallCount++
        _isSubscribed.value = true
    }
}
