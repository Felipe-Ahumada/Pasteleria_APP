package com.pasteleria_app.pasteleria_app.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.*
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import androidx.compose.material3.Text

// 🚀 Rutas principales
sealed class Screen(val route: String) {
    data object Landing : Screen("landing")
    data object Home : Screen("home")
    data object Nosotros : Screen("nosotros")
    data object Carta : Screen("carta")
    data object Contacto : Screen("contacto")
    data object Carrito : Screen("carrito")
    data object Login : Screen("login")
    data object Register : Screen("register")

}

@Composable
fun Navigation(carritoViewModel: CarritoViewModel) { //  Recibe el ViewModel global
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route
    ) {
        // 🎬 Landing
        composable(Screen.Landing.route) {
            LandingPage(
                onEnterClick = { navController.navigate(Screen.Home.route) }
            )
        }

        // 🏠 Home
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                carritoViewModel = carritoViewModel
            )
        }

        // 👩‍🍳 Nosotros
        composable(Screen.Nosotros.route) {
            NosotrosScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                carritoViewModel = carritoViewModel // ✅ también aquí
            )
        }

        // 🍰 Carta
        composable(Screen.Carta.route) {
            CartaScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                carritoViewModel = carritoViewModel
            )
        }

        // 💌 Contacto
        composable(Screen.Contacto.route) {
            ContactoScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                carritoViewModel = carritoViewModel
            )
        }

        // 🛒 Carrito
        composable(Screen.Carrito.route) {
            CarritoScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                carritoViewModel = carritoViewModel
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) }
            )
        }

    }
}
