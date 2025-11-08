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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {}
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }

    val maxChars = 500
    val correoValido = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(correo.trim())
    val formularioValido =
        nombre.isNotBlank() && correoValido && comentario.isNotBlank() && comentario.length <= maxChars

    var mostrarDialogoExito by remember { mutableStateOf(false) }

    PasteleriaScaffold(
        title = "Contáctanos",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(crema),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 🧁 Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // ✉️ Correo
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        isError = correo.isNotBlank() && !correoValido,
                        supportingText = {
                            if (correo.isNotBlank() && !correoValido)
                                Text("Ingresa un correo válido")
                        }
                    )

                    // 💬 Comentario
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = {
                            if (it.length <= maxChars) comentario = it
                        },
                        label = { Text("Comentario") },
                        placeholder = { Text("Máximo 500 caracteres") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 140.dp),
                        supportingText = {
                            Text("${comentario.length}/$maxChars")
                        }
                    )

                    // 📤 Botón Enviar
                    Button(
                        onClick = {
                            if (formularioValido) {
                                mostrarDialogoExito = true
                                nombre = ""
                                correo = ""
                                comentario = ""
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Completa todos los campos correctamente.")
                                }
                            }
                        },
                        enabled = formularioValido,
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        Text(
                            "Enviar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 💌 Diálogo de éxito
            if (mostrarDialogoExito) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogoExito = false },
                    title = {
                        Text(
                            text = "Mensaje enviado",
                            fontWeight = FontWeight.Bold,
                            color = marron,
                            fontSize = 20.sp
                        )
                    },
                    text = {
                        Text(
                            text = "¡Tu mensaje fue enviado con éxito! Te contactaremos pronto.",
                            color = Color(0xFF3E2E20),
                            fontSize = 16.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { mostrarDialogoExito = false },
                            colors = ButtonDefaults.buttonColors(containerColor = marron),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}
