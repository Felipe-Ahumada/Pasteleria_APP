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
    onLoginSuccess: () -> Unit = {}, // Navega al perfil
    onNavigateToRegister: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    carritoViewModel: CarritoViewModel? = null
) {
    val marron = MaterialTheme.colorScheme.primary
    val crema = MaterialTheme.colorScheme.background

    val usuarioViewModel: UsuarioViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var recordar by remember { mutableStateOf(false) }

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
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 👤 Icono superior
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Icono usuario",
                        tint = marron,
                        modifier = Modifier.size(64.dp)
                    )

                    // 🧁 Título
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = marron,
                        textAlign = TextAlign.Center
                    )

                    // 📧 Campo de correo
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 🔒 Campo de contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ☑️ Recordarme + ¿Olvidaste tu contraseña?
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = recordar,
                                onCheckedChange = { recordar = it },
                                colors = CheckboxDefaults.colors(checkedColor = marron)
                            )
                            Text("Recordarme", color = Color(0xFF3E2E20))
                        }

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            color = marron,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { onForgotPassword() }
                        )
                    }

                    // 🔘 Botón principal — validación de login
                    Button(
                        onClick = {
                            scope.launch {
                                when {
                                    correo.isEmpty() || password.isEmpty() -> {
                                        snackbarHostState.showSnackbar("Completa todos los campos ❗")
                                    }

                                    else -> {
                                        val valido = usuarioViewModel.validarUsuario(correo, password)
                                        if (valido) {
                                            snackbarHostState.showSnackbar("Bienvenido de nuevo 🎂")
                                            onLoginSuccess()
                                        } else {
                                            snackbarHostState.showSnackbar("Correo o contraseña incorrectos ❌")
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

                    // 👥 Botones secundarios
                    Button(
                        onClick = { /* TODO: login vendedor */ },
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Entrar como Vendedor", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { /* TODO: login admin */ },
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Entrar como Administrador", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Divider(color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))

                    // 🔗 Registro
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
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

            // 📢 Snackbar inferior
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}
