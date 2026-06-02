package be.howest.howestprime.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snacks")
data class SnackEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val originalPrice: Double,
    val promoPrice: Double?,
    val stock: Int,
    val promo: Boolean,
    val allergens: String
)