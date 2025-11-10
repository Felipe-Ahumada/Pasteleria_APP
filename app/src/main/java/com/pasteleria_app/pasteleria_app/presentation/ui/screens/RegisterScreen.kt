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
import androidx.compose.ui.text.input.VisualTransformation
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
    val crema = Color(0xFFFBF3E9)
    val marron = Color(0xFF3E2E20)
    val blanco = Color.White

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Campos
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

    // Región y comuna
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
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = blanco),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Crear cuenta",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = marron,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Datos personales
                        ValidatedField(
                            label = "Primer nombre *",
                            placeholder = "Ej: María",
                            value = primerNombre,
                            onValueChange = { if (it.length <= 25) primerNombre = it },
                            showError = primerNombre.isBlank(),
                            errorText = "Campo obligatorio"
                        )

                        ValidatedField(
                            label = "Segundo nombre (opcional)",
                            placeholder = "Ej: Luisa",
                            value = segundoNombre,
                            onValueChange = { if (it.length <= 25) segundoNombre = it },
                            showError = false
                        )

                        ValidatedField(
                            label = "Apellido paterno *",
                            placeholder = "Ej: Pérez",
                            value = apellidoPaterno,
                            onValueChange = { if (it.length <= 25) apellidoPaterno = it },
                            showError = apellidoPaterno.isBlank(),
                            errorText = "Campo obligatorio"
                        )

                        ValidatedField(
                            label = "Apellido materno (opcional)",
                            placeholder = "Ej: González",
                            value = apellidoMaterno,
                            onValueChange = { if (it.length <= 25) apellidoMaterno = it },
                            showError = false
                        )

                        ValidatedField(
                            label = "RUN *",
                            placeholder = "Ej: 19011022K",
                            value = run,
                            onValueChange = { if (it.length <= 10) run = it },
                            showError = run.isBlank(),
                            errorText = "Campo obligatorio"
                        )

                        // 🎂 Fecha con auto-slash
                        ValidatedField(
                            label = "Fecha de nacimiento (opcional)",
                            placeholder = "dd/mm/aaaa",
                            value = fechaNacimiento,
                            onValueChange = {
                                val cleaned = it.filter { c -> c.isDigit() }.take(8)
                                fechaNacimiento = when {
                                    cleaned.length >= 5 ->
                                        "${cleaned.take(2)}/${cleaned.drop(2).take(2)}/${cleaned.drop(4)}"
                                    cleaned.length >= 3 ->
                                        "${cleaned.take(2)}/${cleaned.drop(2)}"
                                    else -> cleaned
                                }
                            },
                            showError = fechaNacimiento.isNotBlank() &&
                                    !fechaNacimiento.matches(Regex("^\\d{2}/\\d{2}/\\d{4}\$")),
                            errorText = "Formato inválido (dd/mm/aaaa)"
                        )

                        campoBlanco("Teléfono (opcional)", "+56 9 1234 5678", telefono) { telefono = it }

                        ValidatedField(
                            label = "Correo electrónico *",
                            placeholder = "usuario@dominio.com",
                            value = correo,
                            onValueChange = { correo = it },
                            showError = correo.isBlank(),
                            errorText = "Campo obligatorio"
                        )

                        ValidatedField(
                            label = "Dirección *",
                            placeholder = "Calle 123",
                            value = direccion,
                            onValueChange = { direccion = it },
                            showError = direccion.isBlank(),
                            errorText = "Campo obligatorio"
                        )

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
                                isError = regionSeleccionada.isBlank(),
                                supportingText = {
                                    if (regionSeleccionada.isBlank())
                                        Text(
                                            "Campo obligatorio",
                                            color = MaterialTheme.colorScheme.error,
                                            fontSize = 12.sp
                                        )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandRegion) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
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
                                isError = comunaSeleccionada.isBlank(),
                                supportingText = {
                                    if (comunaSeleccionada.isBlank())
                                        Text(
                                            "Campo obligatorio",
                                            color = MaterialTheme.colorScheme.error,
                                            fontSize = 12.sp
                                        )
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandComuna) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
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

                        // Contraseñas
                        ValidatedField(
                            label = "Contraseña *",
                            placeholder = "Entre 4 y 10 caracteres",
                            value = contrasena,
                            onValueChange = { if (it.length <= 10) contrasena = it },
                            showError = contrasena.isBlank(),
                            errorText = "Campo obligatorio",
                            isPassword = true
                        )

                        ValidatedField(
                            label = "Confirmar contraseña *",
                            placeholder = "Repite tu contraseña",
                            value = confirmarContrasena,
                            onValueChange = { if (it.length <= 10) confirmarContrasena = it },
                            showError = confirmarContrasena != contrasena && confirmarContrasena.isNotBlank(),
                            errorText = "Las contraseñas no coinciden",
                            isPassword = true
                        )

                        campoBlanco(
                            "Código de bienvenida (opcional)",
                            "Ej: MILSABORES2025",
                            codigoBienvenida
                        ) { codigoBienvenida = it }

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

                        Spacer(modifier = Modifier.height(20.dp))

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
                                        primerNombre = primerNombre,
                                        segundoNombre = segundoNombre.takeIf { it.isNotBlank() },
                                        apellidoPaterno = apellidoPaterno,
                                        apellidoMaterno = apellidoMaterno.takeIf { it.isNotBlank() },
                                        rut = run.takeIf { it.isNotBlank() },
                                        direccion = direccion.takeIf { it.isNotBlank() }
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
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatedField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    showError: Boolean,
    errorText: String? = null,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        isError = showError,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        supportingText = {
            if (showError && !errorText.isNullOrEmpty()) {
                Text(text = errorText, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    )
}

@Composable
fun campoBlanco(
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    )
}

/* ✅ VALIDACIONES (misma lógica que tenías) */
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
