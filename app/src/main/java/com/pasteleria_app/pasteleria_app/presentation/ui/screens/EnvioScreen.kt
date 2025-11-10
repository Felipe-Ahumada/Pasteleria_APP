package com.pasteleria_app.pasteleria_app.presentation.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvioScreen(
    onOpenCarrito: () -> Unit = {},
    onConfirmarPago: () -> Unit = {},
    carritoViewModel: CarritoViewModel = hiltViewModel(),
    usuarioViewModel: UsuarioViewModel = hiltViewModel()
) {
    val productos by carritoViewModel.productos.collectAsState()
    val total = carritoViewModel.calcularTotal()

    val crema = Color(0xFFFBF3E9)
    val marron = Color(0xFF3E2E20)
    val blanco = Color.White

    // 🧁 Datos usuario
    val nombre by usuarioViewModel.usuarioActual.collectAsState(initial = "")
    val correo by usuarioViewModel.usuarioCorreo.collectAsState(initial = "")

    var run by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf(nombre ?: "") }
    var apellidos by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("Biobío") }
    var comuna by remember { mutableStateOf("") }
    var fechaEntrega by remember { mutableStateOf("") }
    var tipoEntrega by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("Webpay") }

    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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

            // 🧾 Resumen del carrito
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = blanco),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth()
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
                            ProductoResumenItem(producto)
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

            // 🚚 Información de entrega
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = blanco),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth()
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

                        campoEnvio("RUN", "19011022K", run) { run = it }
                        campoEnvio("Correo", "usuario@correo.com", correo ?: "") {}
                        campoEnvio("Nombres", "Ej: María", nombres ?: "") { nombres = it }
                        campoEnvio("Apellidos", "Ej: González Pérez", apellidos) { apellidos = it }
                        campoEnvio("Dirección", "Ej: Calle 123, Concepción", direccion) { direccion = it }

                        OutlinedTextField(
                            value = region,
                            onValueChange = {},
                            label = { Text("Región") },
                            singleLine = true,
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // 📍 Comuna
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
                                modifier = Modifier.menuAnchor().fillMaxWidth()
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

                        // 📅 Fecha preferida
                        OutlinedTextField(
                            value = fechaEntrega,
                            onValueChange = {},
                            label = { Text("Fecha preferida") },
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { mostrarCalendario = true }) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth, // ✅ corregido
                                        contentDescription = "Elegir fecha"
                                    )
                                }
                            }
                        )

                        // 🚚 Tipo de entrega
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
                                modifier = Modifier.menuAnchor().fillMaxWidth()
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

                        // 💳 Método de pago
                        OutlinedTextField(
                            value = metodoPago,
                            onValueChange = {},
                            label = { Text("Método de Pago") },
                            singleLine = true,
                            readOnly = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botones
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
                                onClick = { onConfirmarPago() },
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

    // 📆 Selector de fecha (retrocompatible)
    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    mostrarCalendario = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val fecha = Date(millis)
                        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fechaEntrega = formato.format(fecha)
                    }
                }) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// 🧁 Producto en resumen
@Composable
fun ProductoResumenItem(producto: Producto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(producto.nombre, color = Color(0xFF3E2E20))
        Text("x${producto.cantidad}", color = Color(0xFF3E2E20))
        Text(formateaCLP(producto.precio * producto.cantidad), color = Color(0xFF3E2E20))
    }
}

// 🧱 Campo reutilizable
@Composable
fun campoEnvio(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun formateaCLP(valor: Int): String =
    "$" + "%,d".format(valor).replace(',', '.')
