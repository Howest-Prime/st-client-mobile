package be.howest.howestprime.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CartItem(
    val snackId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)