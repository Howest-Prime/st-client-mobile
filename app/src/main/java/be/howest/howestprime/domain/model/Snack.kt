package be.howest.howestprime.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Snack(
    val id: Int,
    val name: String,
    val price: Double,
    val promoPrice: Double? = null,
    val imageUrl: String = ""
)