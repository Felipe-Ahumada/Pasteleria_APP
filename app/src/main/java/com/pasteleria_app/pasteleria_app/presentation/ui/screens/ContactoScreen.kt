package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@Composable
fun ContactoScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    carritoViewModel: CarritoViewModel // ✅ necesario para mostrar badge
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    PasteleriaScaffold(
        title = "Contacto",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenLogin = { onOpenLogin() },
        onOpenPerfil = onOpenPerfil,
        carritoViewModel = carritoViewModel // ✅ activa la badge
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(crema)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Contáctanos",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = marron,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Si tienes consultas o sugerencias, envíanos un mensaje y te responderemos a la brevedad.",
                    color = Color(0xFF3E2E20),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                // 🧁 Campo nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = marron,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                // 📧 Campo correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = marron,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                // 💬 Campo mensaje
                OutlinedTextField(
                    value = mensaje,
                    onValueChange = {
                        if (it.length <= 500) mensaje = it
                    },
                    label = { Text("Escribe tu mensaje (máx. 500 caracteres)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = marron,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                // ✉️ Botón enviar
                Button(
                    onClick = {
                        if (nombre.isBlank() || correo.isBlank() || mensaje.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Por favor completa todos los campos.")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Gracias por tu mensaje 💌")
                            }
                            nombre = ""
                            correo = ""
                            mensaje = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = marron),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text(
                        text = "Enviar mensaje",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 📢 Snackbar
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) { snackbarData ->
                Snackbar(
                    snackbarData,
                    containerColor = Color(0xFFF5E9D3),
                    contentColor = Color(0xFF3E2E20),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}
