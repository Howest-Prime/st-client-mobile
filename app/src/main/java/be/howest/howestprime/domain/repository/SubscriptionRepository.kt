package be.howest.howestprime.domain.repository

import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getSubscriptionStatus(): Flow<Boolean>
    suspend fun subscribe(email: String)
}
