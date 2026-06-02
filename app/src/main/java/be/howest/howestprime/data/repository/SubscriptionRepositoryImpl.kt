package be.howest.howestprime.data.repository

import android.util.Log
import be.howest.howestprime.data.local.datastore.SettingsDataStore
import be.howest.howestprime.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val settings: SettingsDataStore
) : SubscriptionRepository {

    override fun getSubscriptionStatus(): Flow<Boolean> {
        return settings.isSubscribed
    }

    override suspend fun subscribe(email: String) {
        Log.i("SubscriptionRepository", "Subscribing email: $email")
        settings.setSubscribed();
    }
}
