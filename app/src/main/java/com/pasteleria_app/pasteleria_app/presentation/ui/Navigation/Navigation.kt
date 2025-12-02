package com.pasteleria_app.pasteleria_app.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.*
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    data object HistorialOrdenes : Screen("historial_ordenes")
    data object DetalleOrden : Screen("detalle_orden/{ordenId}")
    // --- AÑADIDO ---
    data object DetalleProducto : Screen("detalle_producto/{nombre}")
    data object Admin : Screen("admin") // ✅ NUEVO
    data object AdminProductList : Screen("admin_product_list") // ✅ NUEVO
    data object AdminProductEdit : Screen("admin_product_edit/{productoId}") // ✅ NUEVO
    data object AdminOrderList : Screen("admin_order_list") // ✅ NUEVO
    data object AdminUserList : Screen("admin_user_list") // ✅ NUEVO
    data object AdminReports : Screen("admin_reports") // ✅ NUEVO
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
                carritoViewModel = carritoViewModel
            )
        }

        // 🍰 Carta (MODIFICADO)
        composable(Screen.Carta.route) {
            CartaScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                // --- AÑADIDO ---
                onOpenDetalle = { nombre ->
                    // Codificamos el nombre para que los espacios no rompan la URL
                    val encodedNombre = URLEncoder.encode(nombre, StandardCharsets.UTF_8.name())
                    navController.navigate("detalle_producto/$encodedNombre")
                },
                // ---------------
                onOpenAdmin = { navController.navigate(Screen.Admin.route) }, // ✅ NUEVO
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
                onOpenAdmin = { navController.navigate(Screen.Admin.route) }, // ✅ NUEVO
                carritoViewModel = carritoViewModel
            )
        }

        // 🚚 Envio (Corregido: 'EnvioScreen' no necesita todas esas lambdas)
        composable(Screen.Envio.route) {
            EnvioScreen(
                onOpenCarrito = { navController.popBackStack() },
                onNavigateToHistorial = {
                    navController.navigate(Screen.HistorialOrdenes.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
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
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onForgotPassword = { navController.navigate(Screen.ResetPassword.route) },
                carritoViewModel = carritoViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
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

        // 👤 Profile (Corregido: Faltaba carritoViewModel)
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
                onOpenHistorial = {
                    navController.navigate(Screen.HistorialOrdenes.route)
                },
                onOpenAdmin = { navController.navigate(Screen.Admin.route) }, // ✅ NUEVO
                carritoViewModel = carritoViewModel // <-- Añadido
            )
        }


        // 🧾 Historial
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
                onOpenLogin = { navController.navigate(Screen.Login.route) }, // <-- Corregido
                onOpenAdmin = { navController.navigate(Screen.Admin.route) }, // ✅ NUEVO
                carritoViewModel = carritoViewModel,
            )
        }

        // 📄 Detalle de Orden (Corregido: Eliminado duplicado)
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
                    onOpenLogin = { navController.navigate(Screen.Login.route) }, // <-- Corregido
                    onOpenAdmin = { navController.navigate(Screen.Admin.route) }, // ✅ NUEVO
                    carritoViewModel = carritoViewModel
                )
            } else {
                navController.popBackStack()
            }
        }

        // --- 🎂 NUEVO: DETALLE DE PRODUCTO ---
        composable(
            route = Screen.DetalleProducto.route, // "detalle_producto/{nombre}"
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombreProductoUrl = backStackEntry.arguments?.getString("nombre")
            if (nombreProductoUrl != null) {
                DetalleProductoScreen(
                    nombreProductoUrl = nombreProductoUrl,
                    // Pasamos todas las lambdas de navegación al Scaffold
                    onOpenHome = { navController.navigate(Screen.Home.route) },
                    onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                    onOpenCarta = { navController.navigate(Screen.Carta.route) },
                    onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                    onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                    onOpenLogin = { navController.navigate(Screen.Login.route) },
                    onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                    carritoViewModel = carritoViewModel
                )
            } else {
                navController.popBackStack()
            }
        }
        
        // 🔒 Admin Panel
        composable(Screen.Admin.route) {
            AdminScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onOpenProductList = { navController.navigate(Screen.AdminProductList.route) }, // ✅ Conectado
                onOpenOrderList = { navController.navigate(Screen.AdminOrderList.route) }, // ✅ Conectado
                onOpenUserList = { navController.navigate(Screen.AdminUserList.route) }, // ✅ Conectado
                onOpenReports = { navController.navigate(Screen.AdminReports.route) }, // ✅ Conectado
                carritoViewModel = carritoViewModel
            )
        }

        // 📋 Lista de Productos (Admin)
        composable(Screen.AdminProductList.route) {
            AdminProductListScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                onBack = { navController.popBackStack() },
                onEditProduct = { productoId -> 
                    navController.navigate("admin_product_edit/$productoId") 
                },
                carritoViewModel = carritoViewModel
            )
        }

        // ✏️ Editar Producto (Admin)
        composable(
            route = Screen.AdminProductEdit.route,
            arguments = listOf(navArgument("productoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getLong("productoId") ?: 0L
            AdminProductEditScreen(
                productoId = productoId,
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                onBack = { navController.popBackStack() },
                carritoViewModel = carritoViewModel
            )
        }

        // 📦 Lista de Pedidos (Admin)
        composable(Screen.AdminOrderList.route) {
            AdminOrderListScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                onBack = { navController.popBackStack() },
                carritoViewModel = carritoViewModel
            )
        }

        // 👥 Lista de Usuarios (Admin)
        composable(Screen.AdminUserList.route) {
            AdminUserListScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                onBack = { navController.popBackStack() },
                carritoViewModel = carritoViewModel
            )
        }

        // 📊 Reportes (Admin)
        composable(Screen.AdminReports.route) {
            AdminReportScreen(
                onOpenHome = { navController.navigate(Screen.Home.route) },
                onOpenNosotros = { navController.navigate(Screen.Nosotros.route) },
                onOpenCarta = { navController.navigate(Screen.Carta.route) },
                onOpenContacto = { navController.navigate(Screen.Contacto.route) },
                onOpenCarrito = { navController.navigate(Screen.Carrito.route) },
                onOpenLogin = { navController.navigate(Screen.Login.route) },
                onOpenPerfil = { navController.navigate(Screen.Profile.route) },
                onBack = { navController.popBackStack() },
                carritoViewModel = carritoViewModel
            )
        }
    }
}