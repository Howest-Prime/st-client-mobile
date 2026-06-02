package be.howest.howestprime.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.ui.graphics.vector.ImageVector
import be.howest.howestprime.R

sealed class Screen(
    val route: String,
    @StringRes val labelResourceId: Int,
    val icon: ImageVector
) {
    data object Home : Screen("home", R.string.home_label, Icons.Outlined.Home)
    data object Tickets : Screen("tickets", R.string.tickets_label, Icons.Outlined.ConfirmationNumber)
    data object Snacks : Screen("snacks", R.string.snacks_label, Icons.Outlined.Fastfood)
    data object Map : Screen("map", R.string.map_label, Icons.Outlined.Map)
    data object Sensors : Screen("sensors", R.string.sensors_label, Icons.Outlined.FlashlightOn)
    data object Cart : Screen("cart", R.string.cart_label, Icons.Filled.Home)
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Tickets,
    Screen.Snacks,
    Screen.Map,
    Screen.Sensors
)
