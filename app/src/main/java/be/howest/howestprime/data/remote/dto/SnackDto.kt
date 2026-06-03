package be.howest.howestprime.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SnackDto(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val category: String,
    val originalPrice: Double,
    val promoPrice: Double?,
    val stock: Int,
    val promo: Boolean,
    val allergens: List<String>
)
