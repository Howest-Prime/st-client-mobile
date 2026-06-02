package be.howest.howestprime.domain.model

data class CartItem(
    val snackId: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)