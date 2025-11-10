package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.AccountCircle
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    carritoViewModel: CarritoViewModel? = null,
    usuarioViewModel: UsuarioViewModel = hiltViewModel() // ✅ obtenemos el viewmodel
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    val scope = rememberCoroutineScope() // ✅ para corrutinas
    val snackbarHostState = remember { SnackbarHostState() } // ✅ para mensajes visuales

    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }

    PasteleriaScaffold(
        title = "Crear cuenta",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        carritoViewModel = carritoViewModel
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(crema).padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Crear cuenta",
                    tint = marron,
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = "Crear cuenta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = marron,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                campo("Nombre", "Ej: María", nombre) { nombre = it }
                campo("Apellido", "Ej: Pérez", apellidoPaterno) { apellidoPaterno = it }
                campo("Correo electrónico", "usuario@dominio.com", correo) { correo = it }

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    label = { Text("Confirmar contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Checkbox(
                        checked = aceptaTerminos,
                        onCheckedChange = { aceptaTerminos = it },
                        colors = CheckboxDefaults.colors(checkedColor = marron)
                    )
                    Text(
                        text = "Acepto los términos y condiciones",
                        color = marron,
                        modifier = Modifier.clickable { aceptaTerminos = !aceptaTerminos }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            if (correo.isEmpty() || contrasena.isEmpty() || nombre.isEmpty() || !aceptaTerminos) {
                                snackbarHostState.showSnackbar("Completa todos los campos obligatorios ❗")
                                return@launch
                            }

                            if (contrasena != confirmarContrasena) {
                                snackbarHostState.showSnackbar("Las contraseñas no coinciden 🔐")
                                return@launch
                            }

                            val exito = usuarioViewModel.registrarUsuario(
                                correo = correo,
                                contrasena = contrasena,
                                nombre = nombre,
                                apellido = apellidoPaterno
                            )

                            if (exito) {
                                snackbarHostState.showSnackbar("Cuenta creada con éxito 🎉")
                                onOpenLogin()
                            } else {
                                snackbarHostState.showSnackbar("Este correo ya está registrado ⚠️")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = marron),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Crear cuenta", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text("¿Ya tienes cuenta?", color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Inicia sesión",
                        color = marron,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onOpenLogin() }
                    )
                }
            }

            // 📢 Snackbar visual
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
            )
        }
    }
}

// ✅ Campo reutilizable
@Composable
fun campo(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
