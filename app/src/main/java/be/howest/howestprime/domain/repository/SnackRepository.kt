package be.howest.howestprime.domain.repository

import be.howest.howestprime.domain.model.Snack
import kotlinx.coroutines.flow.Flow

interface SnackRepository {
    fun getAllSnacks(): Flow<List<Snack>>
    suspend fun getSnackById(id: String): Snack?
}