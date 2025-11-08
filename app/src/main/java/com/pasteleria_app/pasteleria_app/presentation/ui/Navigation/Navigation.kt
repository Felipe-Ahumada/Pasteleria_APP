package com.pasteleria_app.pasteleria_app.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.HomeScreen
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.NosotrosScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Nosotros : Screen("nosotros")
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            // Pasamos acciones de navegación a HomeScreen
            HomeScreen(
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) }
            )
        }

        composable(Screen.Nosotros.route) {
            NosotrosScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
