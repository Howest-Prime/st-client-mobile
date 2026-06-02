package be.howest.howestprime.domain.repository

import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    val luxFlow: Flow<Float>
    suspend fun isSeated(): Boolean
    suspend fun toggleFlashlight(enabled: Boolean)
}
