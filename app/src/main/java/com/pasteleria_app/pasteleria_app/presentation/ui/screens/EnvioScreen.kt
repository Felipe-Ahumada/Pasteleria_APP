package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.pasteleria_app.pasteleria_app.utils.NotificationHelper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle // <-- AÑADIDO
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import com.pasteleria_app.pasteleria_app.presentation.viewmodel.OrdenViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvioScreen(
    onOpenCarrito: () -> Unit = {},
    onNavigateToHistorial: () -> Unit = {},
    carritoViewModel: CarritoViewModel = hiltViewModel(),
    usuarioViewModel: UsuarioViewModel = hiltViewModel(),
    ordenViewModel: OrdenViewModel = hiltViewModel()
) {
    val productos by carritoViewModel.productos.collectAsState()
    val total = carritoViewModel.calcularTotal()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val crema = Color(0xFFFBF3E9)
    val marron = Color(0xFF3E2E20)
    val blanco = Color.White

    var runUsuario by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("Biobío") }
    var comuna by remember { mutableStateOf("") }
    var fechaEntrega by remember { mutableStateOf("") }
    var tipoEntrega by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("Webpay") }
    var mostrarCalendario by remember { mutableStateOf(false) }

    val isFormValid = remember(runUsuario, nombres, apellidos, direccion, comuna, fechaEntrega, tipoEntrega) {
        runUsuario.isNotBlank() &&
                nombres.isNotBlank() &&
                apellidos.isNotBlank() &&
                direccion.isNotBlank() &&
                comuna.isNotBlank() &&
                fechaEntrega.isNotBlank() &&
                tipoEntrega.isNotBlank()
    }
    val todayMillis = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todayMillis,
        yearRange = 2024..2030,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis
            }
        }
    )
    fun isFechaValida(millis: Long?): Boolean {
        if (millis == null) return false
        return millis >= todayMillis
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val correo by usuarioViewModel.usuarioCorreo.collectAsState(initial = "")
    LaunchedEffect(correo) {
        val correoActual = correo ?: ""
        if (correoActual.isNotBlank()) {
            val usuario = usuarioViewModel.obtenerUsuarioPorCorreo(correoActual)
            usuario?.let { u ->
                runUsuario = u.rut.orEmpty()
                nombres = listOfNotNull(u.primerNombre, u.segundoNombre?.takeIf { it.isNotBlank() })
                    .joinToString(" ")
                apellidos = listOfNotNull(u.apellidoPaterno, u.apellidoMaterno?.takeIf { it.isNotBlank() })
                    .joinToString(" ")
                direccion = u.direccion.orEmpty()
            }
        }
    }
    val usuarioLogueado = !correo.isNullOrBlank()


    PasteleriaScaffold(
        title = "Procesamiento de Pedido",
        onOpenCarrito = onOpenCarrito,
        carritoViewModel = carritoViewModel
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(crema)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = blanco),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Resumen del carrito",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = marron
                        )
                        productos.forEach { producto ->
                            ProductoResumenItem(producto) // <-- Este Composable se actualiza abajo
                        }
                        Divider(color = Color.LightGray)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", fontWeight = FontWeight.Bold, color = marron)
                            Text(formateaCLP(total), fontWeight = FontWeight.Bold, color = marron)
                        }
                    }
                }
            }
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = blanco),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Información de Entrega",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = marron
                        )
                        campoEnvio("RUN", "19011022K", runUsuario, { runUsuario = it }, usuarioLogueado)
                        campoEnvio("Correo", "usuario@correo.com", correo ?: "", {}, true)
                        campoEnvio("Nombres", "Ej: María", nombres, { nombres = it }, usuarioLogueado)
                        campoEnvio("Apellidos", "Ej: González Pérez", apellidos, { apellidos = it }, usuarioLogueado)
                        campoEnvio("Dirección", "Ej: Calle 123, Concepción", direccion, { direccion = it }, usuarioLogueado)
                        OutlinedTextField(
                            value = region,
                            onValueChange = {},
                            label = { Text("Región") },
                            readOnly = true,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        val comunas = listOf("Concepción", "Talcahuano", "Hualpén")
                        var expandirComuna by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandirComuna,
                            onExpandedChange = { expandirComuna = it }
                        ) {
                            OutlinedTextField(
                                value = comuna,
                                onValueChange = {},
                                label = { Text("Comuna") },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandirComuna) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandirComuna,
                                onDismissRequest = { expandirComuna = false }
                            ) {
                                comunas.forEach { opcion ->
                                    DropdownMenuItem(
                                        text = { Text(opcion) },
                                        onClick = {
                                            comuna = opcion
                                            expandirComuna = false
                                        }
                                    )
                                }
                            }
                        }
                        OutlinedTextField(
                            value = fechaEntrega,
                            onValueChange = {},
                            label = { Text("Fecha preferida") },
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { mostrarCalendario = true }) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = "Elegir fecha")
                                }
                            }
                        )
                        val tiposEntrega = listOf("Despacho a domicilio", "Retiro en tienda")
                        var expandirTipo by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandirTipo,
                            onExpandedChange = { expandirTipo = it }
                        ) {
                            OutlinedTextField(
                                value = tipoEntrega,
                                onValueChange = {},
                                label = { Text("Tipo de entrega") },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandirTipo) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandirTipo,
                                onDismissRequest = { expandirTipo = false }
                            ) {
                                tiposEntrega.forEach { opcion ->
                                    DropdownMenuItem(
                                        text = { Text(opcion) },
                                        onClick = {
                                            tipoEntrega = opcion
                                            expandirTipo = false
                                        }
                                    )
                                }
                            }
                        }
                        OutlinedTextField(
                            value = metodoPago,
                            onValueChange = {},
                            label = { Text("Método de Pago") },
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = onOpenCarrito,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Volver al carrito")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val pedidoId = ordenViewModel.crearOrden(
                                            productos = productos,
                                            total = total,
                                            direccion = direccion,
                                            comuna = comuna,
                                            fechaEntrega = fechaEntrega,
                                            tipoEntrega = tipoEntrega
                                        )
                                        carritoViewModel.vaciarCarrito()
                                        NotificationHelper.showOrderConfirmationNotification(
                                            context,
                                            pedidoId,
                                            total
                                        )
                                        onNavigateToHistorial()
                                    }
                                },
                                enabled = isFormValid,
                                colors = ButtonDefaults.buttonColors(containerColor = marron),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Confirmar y Pagar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (isFechaValida(millis)) {
                        mostrarCalendario = false
                        val fecha = Date(millis!!)
                        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fechaEntrega = formato.format(fecha)
                    }
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = true,
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFFFDF8F4),
                    selectedDayContainerColor = Color(0xFF6D4C41),
                    selectedDayContentColor = Color.White,
                    disabledDayContentColor = Color(0xFFBDBDBD),
                    todayContentColor = Color(0xFF6D4C41),
                    weekdayContentColor = Color(0xFF5D4037),
                    subheadContentColor = Color(0xFF5D4037)
                )
            )
        }
    }
}

// ---- MODIFICADO AQUÍ ----
@Composable
fun ProductoResumenItem(producto: Producto) {
    // Se envuelve en un Column para mostrar el mensaje opcional
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(producto.nombre, color = Color(0xFF3E2E20), fontWeight = FontWeight.Medium)
            Text("x${producto.cantidad}", color = Color(0xFF3E2E20))
            Text(formateaCLP(producto.precio * producto.cantidad), color = Color(0xFF3E2E20))
        }
        // --- AÑADIDO ---
        if (!producto.mensaje.isNullOrBlank()) {
            Text(
                text = "Mensaje: \"${producto.mensaje}\"",
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF6D4C41), // Un marrón más suave
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
        // ---------------
    }
}
// -------------------------

@Composable
fun campoEnvio(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        readOnly = readOnly,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(if (readOnly) Color(0xFFF8F8F8) else Color.Transparent),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF3E2E20),
            focusedBorderColor = if (readOnly) Color.LightGray else Color(0xFF3E2E20),
            unfocusedBorderColor = if (readOnly) Color.LightGray else Color.Gray
        )
    )
}

private fun formateaCLP(valor: Int): String =
    "$" + "%,d".format(valor).replace(',', '.')