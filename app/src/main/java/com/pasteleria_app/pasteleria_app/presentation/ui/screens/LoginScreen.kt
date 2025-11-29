package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    carritoViewModel: CarritoViewModel = hiltViewModel()
) {
    val marron = MaterialTheme.colorScheme.primary
    val crema = MaterialTheme.colorScheme.background
    val usuarioViewModel: UsuarioViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    PasteleriaScaffold(
        title = "Iniciar Sesión",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        carritoViewModel = carritoViewModel
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(crema)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = marron,
                        modifier = Modifier.size(64.dp)
                    )

                    Text("Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = marron)

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            scope.launch {
                                when {
                                    correo.isEmpty() || password.isEmpty() -> {
                                        snackbarHostState.showSnackbar("Completa todos los campos ❗")
                                    }
                                    else -> {
                                        val valido = usuarioViewModel.validarUsuario(correo.trim(), password.trim())
                                        if (valido) {
                                            // cargar carrito del usuario que acaba de iniciar sesión
                                            carritoViewModel?.cargarCarritoUsuario(correo)

                                            snackbarHostState.showSnackbar("Bienvenido de nuevo 🎂")
                                            onLoginSuccess() // → navega a Profile (o a donde definiste)
                                        } else {
                                            snackbarHostState.showSnackbar("Credenciales incorrectas ❌")
                                        }

                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Ingresar", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Divider(color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))

                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Text("¿No tienes cuenta?", color = Color.DarkGray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Crear cuenta",
                            color = marron,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToRegister() }
                        )
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
            )
        }
    }
}
