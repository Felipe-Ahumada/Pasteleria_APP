package com.pasteleria_app.pasteleria_app.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.*

// 🚀 Definimos las rutas principales de la app
sealed class Screen(val route: String) {
    data object Landing : Screen("landing")
    data object Home : Screen("home")
    data object Nosotros : Screen("nosotros")
    data object Carta : Screen("carta")
    data object Contacto : Screen("contacto")
    data object Carrito : Screen("carrito")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // 👇 Puedes cambiar a Screen.Home.route si prefieres empezar ahí
        startDestination = Screen.Landing.route
    ) {
        // 🎬 Landing Page
        composable(Screen.Landing.route) {
            LandingPage(
                onEnterClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🏠 Home
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenNosotros = {
                    navController.navigate(Screen.Nosotros.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarta = {
                    navController.navigate(Screen.Carta.route) {
                        launchSingleTop = true
                    }
                },
                onOpenContacto = {
                    navController.navigate(Screen.Contacto.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarrito = {
                    navController.navigate(Screen.Carrito.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 👩‍🍳 Nosotros
        composable(Screen.Nosotros.route) {
            NosotrosScreen(
                onOpenHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenNosotros = {
                    navController.navigate(Screen.Nosotros.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarta = {
                    navController.navigate(Screen.Carta.route) {
                        launchSingleTop = true
                    }
                },
                onOpenContacto = {
                    navController.navigate(Screen.Contacto.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarrito = {
                    navController.navigate(Screen.Carrito.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🍰 Carta
        composable(Screen.Carta.route) {
            CartaScreen(
                onOpenHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenNosotros = {
                    navController.navigate(Screen.Nosotros.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarta = {
                    navController.navigate(Screen.Carta.route) {
                        launchSingleTop = true
                    }
                },
                onOpenContacto = {
                    navController.navigate(Screen.Contacto.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarrito = {
                    navController.navigate(Screen.Carrito.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 💌 Contacto
        composable(Screen.Contacto.route) {
            ContactoScreen(
                onOpenHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenNosotros = {
                    navController.navigate(Screen.Nosotros.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarta = {
                    navController.navigate(Screen.Carta.route) {
                        launchSingleTop = true
                    }
                },
                onOpenContacto = {
                    navController.navigate(Screen.Contacto.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarrito = {
                    navController.navigate(Screen.Carrito.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 🛒 Carrito
        composable(Screen.Carrito.route) {
            CarritoScreen(
                onOpenHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onOpenNosotros = {
                    navController.navigate(Screen.Nosotros.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarta = {
                    navController.navigate(Screen.Carta.route) {
                        launchSingleTop = true
                    }
                },
                onOpenContacto = {
                    navController.navigate(Screen.Contacto.route) {
                        launchSingleTop = true
                    }
                },
                onOpenCarrito = {
                    navController.navigate(Screen.Carrito.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
