package be.howest.howestprime.domain.model

data class Snack(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val originalPrice: Double,
    val promoPrice: Double?,
    val stock: Int,
    val promo: Boolean,
    val allergens: List<String>
)