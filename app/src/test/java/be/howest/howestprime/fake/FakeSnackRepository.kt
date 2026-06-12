package be.howest.howestprime.fake

import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.repository.SnackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSnackRepository : SnackRepository {

    private val _snacks = MutableStateFlow<List<Snack>>(emptyList())
    var shouldThrowOnGetAll = false

    fun setSnacks(snacks: List<Snack>) {
        _snacks.value = snacks
    }

    override fun getAllSnacks(): Flow<List<Snack>> {
        if (shouldThrowOnGetAll) throw RuntimeException("Failed to load snacks")
        return _snacks.asStateFlow()
    }

    override suspend fun getSnackById(id: String): Snack? =
        _snacks.value.firstOrNull { it.id == id }
}
