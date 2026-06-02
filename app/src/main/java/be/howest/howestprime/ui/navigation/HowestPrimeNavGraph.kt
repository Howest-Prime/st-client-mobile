package be.howest.howestprime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.howest.howestprime.ui.cart.CartScreen
import be.howest.howestprime.ui.cart.CartViewModel
import be.howest.howestprime.ui.home.HomeScreen
import be.howest.howestprime.ui.home.HomeViewModel
import be.howest.howestprime.ui.map.MapScreen
import be.howest.howestprime.ui.map.MapViewModel
import be.howest.howestprime.ui.sensors.SensorsScreen
import be.howest.howestprime.ui.sensors.SensorsViewModel
import be.howest.howestprime.ui.snacks.SnacksScreen
import be.howest.howestprime.ui.snacks.SnacksViewModel
import be.howest.howestprime.ui.tickets.TicketsScreen
import be.howest.howestprime.ui.tickets.TicketsViewModel

@Composable
fun HowestPrimeNavGraph(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    ticketsViewModel: TicketsViewModel = hiltViewModel(),
    snacksViewModel: SnacksViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    sensorsViewModel: SensorsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val ticketsUiState by ticketsViewModel.uiState.collectAsStateWithLifecycle()
    val snacksUiState by snacksViewModel.uiState.collectAsStateWithLifecycle()
    val cartUiState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val mapUiState by mapViewModel.uiState.collectAsStateWithLifecycle()
    val sensorsUiState by sensorsViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                uiState = homeUiState,
                onOrderSnackClick = { navController.navigate(Screen.Snacks.route) },
                onEmailChanged = { homeViewModel.onEmailChanged(it) },
                onSubscribeClick = { homeViewModel.onSubscribeClick() }
            )
        }

        composable(Screen.Tickets.route) {
            TicketsScreen(
                uiState = ticketsUiState,
                onToggleUpcomingOnly = { ticketsViewModel.toggleUpcomingOnly(it) }
            )
        }

        composable(Screen.Snacks.route) {
            SnacksScreen(
                uiState = snacksUiState,
                onAddToCart = { snacksViewModel.addToCart(it) }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                uiState = cartUiState,
                onPlaceOrder = { cartViewModel.placeOrder() },
                onClearCart = { cartViewModel.clearCart(); navController.popBackStack() }
            )
        }

        composable(Screen.Map.route) {
            MapScreen(
                uiState = mapUiState,
                onPermissionGranted = { mapViewModel.onPermissionGranted() },
                onPermissionDenied = { mapViewModel.onPermissionDenied() }
            )
        }

        composable(Screen.Sensors.route) {
            SensorsScreen(
                uiState = sensorsUiState,
                onToggleFlashlight = { sensorsViewModel.toggleFlashlight(it) }
            )
        }
    }
}