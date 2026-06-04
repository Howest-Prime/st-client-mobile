package be.howest.howestprime.data.repository

import android.util.Log
import be.howest.howestprime.data.local.room.dao.SnackDao
import be.howest.howestprime.data.local.room.entity.SnackEntity
import be.howest.howestprime.data.remote.SnackApiClient
import be.howest.howestprime.data.remote.dto.SnackDto
import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.repository.SnackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackRepositoryImpl @Inject constructor(
    private val api: SnackApiClient,
    private val dao: SnackDao
) : SnackRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dtos = api.getSnacks()
                dao.insertAll(dtos.map { it.toEntity() })
                Log.i("SnackRepository", "Fetched ${dtos.size} snacks from API and saved to Room ")
            } catch (e: Exception) {
                Log.e("SnackRepository", "Failed to fetch snacks from API: ${e.message}")
            }
        }
    }

    override fun getAllSnacks(): Flow<List<Snack>> {
        return dao.getAllSnacks().map { entities -> 
            entities.map { it.toDomain() } 
        }
    }

    override suspend fun getSnackById(id: String): Snack? {
        return dao.getSnackById(id)?.toDomain()
    }
}

private fun SnackDto.toEntity() = SnackEntity(
    id = id,
    name = name,
    description = description,
    imageUrl = image,
    category = category,
    originalPrice = originalPrice,
    promoPrice = promoPrice,
    stock = stock,
    promo = promo,
    allergens = allergens.joinToString(",")
)

private fun SnackDto.toDomain() = Snack(
    id = id,
    name = name,
    description = description,
    imageUrl = image,
    category = category,
    originalPrice = originalPrice,
    promoPrice = promoPrice,
    stock = stock,
    promo = promo,
    allergens = allergens
)

private fun SnackEntity.toDomain() = Snack(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    category = category,
    originalPrice = originalPrice,
    promoPrice = promoPrice,
    stock = stock,
    promo = promo,
    allergens = allergens.split(",")
)
