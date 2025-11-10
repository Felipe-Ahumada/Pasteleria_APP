package com.pasteleria_app.pasteleria_app.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.*
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

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
    data object ResetPassword : Screen("reset_password")
    data object Profile : Screen("profile")
    data object Envio : Screen("envio")

    // --- AÑADIDAS ---
    data object HistorialOrdenes : Screen("historial_ordenes")
    data object DetalleOrden : Screen("detalle_orden/{ordenId}")
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

        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
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
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
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
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
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
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
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
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                onOpenEnvio = { navController.navigate(Screen.Envio.route) },
                carritoViewModel = carritoViewModel
            )
        }

        // ---- MODIFICADO ----
        composable(Screen.Envio.route) {
            EnvioScreen(
                onOpenCarrito = { navController.popBackStack() }, // Vuelve a la pantalla anterior (Carrito)
                onNavigateToHistorial = {
                    navController.navigate(Screen.HistorialOrdenes.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                carritoViewModel = carritoViewModel

            )
        }
        // --------------------

        composable(Screen.Login.route) {
            LoginScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onForgotPassword = { navController.navigate(Screen.ResetPassword.route) },
                carritoViewModel = carritoViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // elimina login del stack
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                carritoViewModel = carritoViewModel
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onBackToLogin = { navController.navigate(Screen.Login.route) },
                carritoViewModel = carritoViewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onOpenHistorial = { // <-- AÑADIDO
                    navController.navigate(Screen.HistorialOrdenes.route)
                }
            )
        }
        // --------------------


        composable(Screen.HistorialOrdenes.route) {
            HistorialOrdenesScreen(
                onNavigateToDetalle = { ordenId ->
                    navController.navigate("detalle_orden/$ordenId")
                },
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                carritoViewModel = carritoViewModel,
            )
        }
        // --------------------
        composable(
            route = Screen.DetalleOrden.route, // "detalle_orden/{ordenId}"
            arguments = listOf(navArgument("ordenId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ordenId = backStackEntry.arguments?.getString("ordenId")
            if (ordenId != null) {
                DetalleOrdenScreen(
                    ordenId = ordenId,
                    onOpenHome = { navController.navigate(Screen.Home.route) },
                    onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                    onOpenCarta = { navController.navigate(Screen.Carta.route) },
                    onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                    onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                    onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                    carritoViewModel = carritoViewModel
                )
            } else {
                // Si no hay ID, simplemente vuelve atrás
                navController.popBackStack()
            }
        }
        // --------------------

    }
}