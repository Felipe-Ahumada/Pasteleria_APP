package com.pasteleria_app.pasteleria_app
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.CartaScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.HomeScreen
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.LandingPage
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.NosotrosScreen
import com.pasteleria_app.pasteleria_app.presentation.ui.theme.Pasteleria_APPTheme
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.ContactoScreen
import com.pasteleria_app.pasteleria_app.presentation.ui.screens.CarritoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pasteleria_APPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Controlador de navegación
                    val navController = rememberNavController()

                    // Estructura de navegación principal
                    NavHost(navController = navController, startDestination = "home") {
                        composable("landing") { LandingPage(navController) }

                        composable("home") {
                            HomeScreen(
                                onOpenNosotros = { navController.navigate("nosotros") },
                                onOpenCarta = { navController.navigate("carta") },
                                onOpenContacto = { navController.navigate("contacto") },
                                onOpenCarrito = { navController.navigate("carrito") } // ✅ NUEVO
                            )
                        }

                        composable("nosotros") { NosotrosScreen(onBackClick = { navController.popBackStack() }) }
                        composable("carta") { CartaScreen(onBackClick = { navController.popBackStack() }) }
                        composable("contacto") { ContactoScreen(onBackClick = { navController.popBackStack() }) }
                        composable("carrito") {
                            CarritoScreen(
                                onBackClick = { navController.popBackStack() },
                                onVerProductos = { navController.navigate("carta") }
                            )
                        }
                    }
                }
            }
        }
    }
}
