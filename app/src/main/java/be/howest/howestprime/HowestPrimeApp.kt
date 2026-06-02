package be.howest.howestprime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.howest.howestprime.ui.cart.CartViewModel
import be.howest.howestprime.ui.navigation.HowestPrimeAppBar
import be.howest.howestprime.ui.navigation.HowestPrimeBottomBar
import be.howest.howestprime.ui.navigation.HowestPrimeNavGraph
import be.howest.howestprime.ui.navigation.Screen
import be.howest.howestprime.ui.theme.HowestprimeTheme

@Composable
fun HowestPrimeApp() {
    HowestprimeTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val cartViewModel = hiltViewModel<CartViewModel>()
        val cartUiState by cartViewModel.uiState.collectAsStateWithLifecycle()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                HowestPrimeAppBar(
                    appName = R.string.app_name,
                    canNavigateBack = currentDestination?.route == Screen.Cart.route,
                    navigateUp = { navController.navigateUp() },
                    cartItemCount = cartUiState.totalItems,
                    onCartClick = { navController.navigate(Screen.Cart.route) }
                )
            },
            bottomBar = {
                HowestPrimeBottomBar(
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        ) { innerPadding ->
            HowestPrimeNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
