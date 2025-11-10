package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    usuarioViewModel: UsuarioViewModel = hiltViewModel()
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // 🧁 Campos del formulario
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var run by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var codigoBienvenida by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }

    // 🗺️ Región y comuna
    val regiones = listOf("Biobío")
    val comunas = listOf("Concepción", "Talcahuano", "Hualpén")
    var regionSeleccionada by remember { mutableStateOf("") }
    var comunaSeleccionada by remember { mutableStateOf("") }
    var expandRegion by remember { mutableStateOf(false) }
    var expandComuna by remember { mutableStateOf(false) }

    PasteleriaScaffold(
        title = "Crear cuenta",
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
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🧾 Título
                Text(
                    text = "Crear cuenta",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = marron,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 🧍 Datos personales
                campo("Primer nombre *", "Ej: María", primerNombre) { primerNombre = it }
                campo("Segundo nombre (opcional)", "Ej: Luisa", segundoNombre) { segundoNombre = it }
                campo("Apellido paterno *", "Ej: Pérez", apellidoPaterno) { apellidoPaterno = it }
                campo("Apellido materno (opcional)", "Ej: González", apellidoMaterno) { apellidoMaterno = it }
                campo("RUN *", "Ej: 19011022K", run) { run = it }

                campo("Fecha de nacimiento (opcional)", "dd/mm/aaaa", fechaNacimiento) { fechaNacimiento = it }
                campo("Teléfono (opcional)", "+56 9 1234 5678", telefono) { telefono = it }

                // 📬 Dirección y contacto
                campo("Correo electrónico *", "usuario@dominio.com", correo) { correo = it }
                campo("Dirección *", "Calle 123", direccion) { direccion = it }

                // Región
                ExposedDropdownMenuBox(
                    expanded = expandRegion,
                    onExpandedChange = { expandRegion = !expandRegion }
                ) {
                    OutlinedTextField(
                        value = regionSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Región *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandRegion) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandRegion,
                        onDismissRequest = { expandRegion = false }
                    ) {
                        regiones.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(region) },
                                onClick = {
                                    regionSeleccionada = region
                                    expandRegion = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Comuna
                ExposedDropdownMenuBox(
                    expanded = expandComuna,
                    onExpandedChange = { expandComuna = !expandComuna }
                ) {
                    OutlinedTextField(
                        value = comunaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Comuna *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandComuna) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandComuna,
                        onDismissRequest = { expandComuna = false }
                    ) {
                        comunas.forEach { comuna ->
                            DropdownMenuItem(
                                text = { Text(comuna) },
                                onClick = {
                                    comunaSeleccionada = comuna
                                    expandComuna = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔐 Contraseña
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña *") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    label = { Text("Confirmar contraseña *") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                campo("Código de bienvenida (opcional)", "Ej: MILSABORES2025", codigoBienvenida) {
                    codigoBienvenida = it
                }

                // ✅ Checkbox términos
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

                // 🧠 Botón Crear cuenta
                Button(
                    onClick = {
                        scope.launch {
                            val errores = validarCampos(
                                primerNombre, apellidoPaterno, run, correo,
                                direccion, regionSeleccionada, comunaSeleccionada,
                                contrasena, confirmarContrasena, aceptaTerminos, fechaNacimiento
                            )

                            if (errores.isNotEmpty()) {
                                snackbarHostState.showSnackbar(errores.first())
                                return@launch
                            }

                            val exito = usuarioViewModel.registrarUsuario(
                                correo = correo,
                                contrasena = contrasena,
                                nombre = "$primerNombre $apellidoPaterno",
                                apellido = apellidoMaterno
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

// ✅ Validaciones
fun validarCampos(
    primerNombre: String,
    apellidoPaterno: String,
    run: String,
    correo: String,
    direccion: String,
    region: String,
    comuna: String,
    contrasena: String,
    confirmarContrasena: String,
    aceptaTerminos: Boolean,
    fechaNacimiento: String
): List<String> {
    val errores = mutableListOf<String>()

    if (primerNombre.isBlank()) errores.add("El primer nombre es obligatorio ❗")
    if (apellidoPaterno.isBlank()) errores.add("El apellido paterno es obligatorio ❗")
    if (run.isBlank()) errores.add("El RUN es obligatorio ❗")
    else if (!run.matches(Regex("^[0-9]{7,8}[0-9Kk]\$"))) errores.add("RUN inválido ⚠️")

    if (correo.isBlank()) errores.add("El correo es obligatorio ❗")
    else if (!correo.matches(Regex("^[\\w.-]+@[\\w.-]+\\.\\w+\$"))) errores.add("Correo inválido ⚠️")

    if (direccion.isBlank()) errores.add("La dirección es obligatoria ❗")
    if (region.isBlank()) errores.add("La región es obligatoria ❗")
    if (comuna.isBlank()) errores.add("La comuna es obligatoria ❗")

    if (contrasena.length !in 4..10)
        errores.add("La contraseña debe tener entre 4 y 10 caracteres 🔒")

    if (confirmarContrasena != contrasena)
        errores.add("Las contraseñas no coinciden 🔐")

    if (fechaNacimiento.isNotBlank() && !fechaNacimiento.matches(Regex("^\\d{2}/\\d{2}/\\d{4}\$")))
        errores.add("Formato de fecha inválido (usa dd/mm/aaaa) 📅")

    if (!aceptaTerminos) errores.add("Debes aceptar los términos y condiciones ✅")

    return errores
}
