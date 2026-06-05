package be.howest.howestprime.domain.repository

import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    val luxFlow: Flow<Float>
    fun start()
    fun stop()
    val isSeated : Flow<Boolean>
    suspend fun toggleFlashlight(enabled: Boolean)
}
