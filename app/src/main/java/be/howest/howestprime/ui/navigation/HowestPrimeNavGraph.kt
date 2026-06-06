package be.howest.howestprime.ui.navigation

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import be.howest.howestprime.ui.permissions.AppPermission
import be.howest.howestprime.ui.permissions.MainViewModel
import be.howest.howestprime.ui.permissions.PermissionManager
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
    mainViewModel: MainViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    ticketsViewModel: TicketsViewModel = hiltViewModel(),
    snacksViewModel: SnacksViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    sensorsViewModel: SensorsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val ticketsUiState by ticketsViewModel.uiState.collectAsStateWithLifecycle()
    val snacksUiState by snacksViewModel.uiState.collectAsStateWithLifecycle()
    val cartUiState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val mapUiState by mapViewModel.uiState.collectAsStateWithLifecycle()
    val sensorsUiState by sensorsViewModel.uiState.collectAsStateWithLifecycle()

    val permissionRequest by mainViewModel.permissionRequest.collectAsStateWithLifecycle()
    val deniedPermission by mainViewModel.deniedPermission.collectAsStateWithLifecycle()

    LaunchedEffect(deniedPermission) {
        deniedPermission ?: return@LaunchedEffect
        val message = when (deniedPermission) {
            AppPermission.NOTIFICATIONS -> "Notification permission is required to set movie reminders."
            AppPermission.LOCATION -> "Location permission is required to show your position on the map."
            null -> return@LaunchedEffect
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        mainViewModel.onDeniedMessageShown()
    }

    PermissionManager(
        permissionRequest = permissionRequest,
        onPermissionResult = { permission, granted ->
            mainViewModel.onPermissionResult(permission, granted)
            if (granted) {
                when (permission) {
                    AppPermission.LOCATION -> mapViewModel.startLocationUpdates()
                    AppPermission.NOTIFICATIONS -> ticketsViewModel.schedulePendingReminder()
                    else -> {}
                }
            }
        }
    )

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
                onSubscribeClick = { 
                    homeViewModel.onSubscribeClick() 
                    Toast.makeText(context, "Subscribed to newsletter.", Toast.LENGTH_SHORT).show()
                    }
            )
        }

        composable(Screen.Tickets.route) {
            TicketsScreen(
                uiState = ticketsUiState,
                onToggleUpcomingOnly = { ticketsViewModel.toggleUpcomingOnly() },
                onMovieReminderClick = { ticket, minutesBefore ->
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            ticketsViewModel.scheduleReminder(ticket, minutesBefore)
                        }
                        else -> {
                            ticketsViewModel.setPendingReminder(ticket, minutesBefore)
                            mainViewModel.requestPermission(AppPermission.NOTIFICATIONS)
                        }
                    }
                }
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
                onRequestLocationPermission = {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            mapViewModel.startLocationUpdates()
                        }
                        else -> {
                            mainViewModel.requestPermission(AppPermission.LOCATION)
                        }
                    }
                },
                onLocationPermissionGranted = { mapViewModel.startLocationUpdates() },
                onMapReady = {},
                onMapDisposed = {},
            )
        }

        composable(Screen.Sensors.route) {
            SensorsScreen(
                uiState = sensorsUiState,
                toggleFlashLight = { sensorsViewModel.toggleFlashlight() },
                onScreenVisible = { sensorsViewModel.onScreenVisible() },
                onScreenHidden = { sensorsViewModel.onScreenHidden() }
            )
        }
    }
}