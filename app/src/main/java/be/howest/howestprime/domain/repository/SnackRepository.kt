package be.howest.howestprime.domain.repository

import be.howest.howestprime.domain.model.Snack

interface SnackRepository {
    suspend fun getAllSnacks(): List<Snack>
    suspend fun getSnackById(id: Int): Snack?
}