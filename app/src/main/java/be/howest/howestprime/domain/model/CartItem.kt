package be.howest.howestprime.domain.model

data class CartItem(
    val snackId: String,
    val name: String,
    val price: Double,
    val quantity: Int
)