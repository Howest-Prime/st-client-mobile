package be.howest.howestprime.data.repository

import be.howest.howestprime.data.local.room.dao.SnackDao
import be.howest.howestprime.data.local.room.entity.SnackEntity
import be.howest.howestprime.data.remote.SnackApiClient
import be.howest.howestprime.data.remote.dto.SnackDto
import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.repository.SnackRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackRepositoryImpl @Inject constructor(
    private val api: SnackApiClient,
    private val dao: SnackDao
) : SnackRepository {

    override suspend fun getAllSnacks(): List<Snack> {
        val dtos = api.getSnacks()
        dao.insertAll(dtos.map { it.toEntity() })
        return dtos.map { it.toDomain() }
    }

    override suspend fun getSnackById(id: String): Snack? {
        return dao.getSnackById(id)?.toDomain()
            ?: getAllSnacks().find { it.id == id }
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
