package be.howest.howestprime.data.repository

import be.howest.howestprime.data.remote.SnackApiClient
import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.domain.repository.SnackRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackRepositoryImpl @Inject constructor(
    private val api: SnackApiClient
) : SnackRepository {

    override suspend fun getAllSnacks(): List<Snack> {
        return api.getSnacks().map { dto ->
            Snack(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                imageUrl = dto.image,
                category = dto.category,
                originalPrice = dto.originalPrice,
                promoPrice = dto.promoPrice,
                stock = dto.stock,
                promo = dto.promo,
                allergens = dto.allergens
            )
        }
    }

    override suspend fun getSnackById(id: String): Snack? {
        return api.getSnacks()
            .map { dto ->
                Snack(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    imageUrl = dto.image,
                    category = dto.category,
                    originalPrice = dto.originalPrice,
                    promoPrice = dto.promoPrice,
                    stock = dto.stock,
                    promo = dto.promo,
                    allergens = dto.allergens
                )
            }
            .find { it.id == id }
    }
}
