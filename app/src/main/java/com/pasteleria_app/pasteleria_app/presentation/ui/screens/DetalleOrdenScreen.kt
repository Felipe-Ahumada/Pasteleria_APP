package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.pasteleria_app.pasteleria_app.utils.BoletaPdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle // <-- AÑADIDO
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.domain.model.OrdenItem
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.viewmodel.OrdenViewModel
import com.pasteleria_app.pasteleria_app.utils.formateaCLP

@Composable
fun DetalleOrdenScreen(
    ordenId: String,
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    onOpenCarrito: () -> Unit,
    onOpenAdmin: () -> Unit = {}, // ✅ NUEVO
    carritoViewModel: CarritoViewModel = hiltViewModel(),
    ordenViewModel: OrdenViewModel = hiltViewModel()
) {
    val orden by ordenViewModel.ordenSeleccionada.collectAsState()
    val crema = Color(0xFFFBF3E9)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(ordenId) {
        ordenViewModel.cargarOrdenPorId(ordenId)
    }

    PasteleriaScaffold(
        title = "Detalle del Pedido",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenLogin = onOpenLogin,
        onOpenPerfil = onOpenPerfil,
        onOpenAdmin = onOpenAdmin, // ✅ NUEVO
        carritoViewModel = carritoViewModel
    ) { padding ->
        orden?.let { ordenDetalle ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(crema)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card de Seguimiento
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Seguimiento", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Pedido: ${ordenDetalle.id}")
                                Text("Tracking: ${ordenDetalle.trackingId}")
                            }
                            Spacer(Modifier.height(24.dp))
                            TimelineSeguimiento(estadoActual = ordenDetalle.estado)
                        }
                    }
                }

                // Card de Detalle de Compra
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        DetalleCompra(ordenDetalle) // <-- Este Composable se actualiza abajo
                    }
                }

                // Card de Envío
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        DetalleEnvio(ordenDetalle)
                    }
                }

                // Botón Imprimir
                item {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val pdfUri = withContext(Dispatchers.IO) {
                                        BoletaPdfGenerator.generar(context, ordenDetalle)
                                    }
                                    compartirPdf(context, pdfUri, ordenDetalle.id)
                                } catch (e: android.content.ActivityNotFoundException) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "No se encontró una app para abrir el PDF", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5), contentColor = Color.Black),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("Imprimir boleta")
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun TimelineSeguimiento(estadoActual: String) {
    val estados = listOf("Preparación", "En camino", "Entregado")
    val estadoIndex = estados.indexOf(estadoActual)

    val colorActivo = Color(0xFF3E2E20)
    val colorInactivo = Color.LightGray

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        estados.forEachIndexed { index, estado ->
            val esActivo = index <= estadoIndex
            val color = if (esActivo) colorActivo else colorInactivo

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color, CircleShape)
                )
                Text(estado, fontSize = 12.sp, color = color, modifier = Modifier.padding(top = 4.dp))
            }

            if (index < estados.size - 1) {
                Box(
                    Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(color)
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}

// ---- MODIFICADO AQUÍ ----
@Composable
fun DetalleCompra(orden: Orden) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Detalle", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        // Encabezados
        Row(Modifier.fillMaxWidth()) {
            Text("Producto", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text("Cant.", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold)
            Text("Precio", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Subtotal", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }
        Divider()
        // Items
        orden.items.forEach { item ->
            // Se envuelve en un Column para mostrar el mensaje opcional
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(item.nombreProducto, modifier = Modifier.weight(2f))
                    Text("${item.cantidad}", modifier = Modifier.weight(0.5f))
                    Text(formateaCLP(item.precioUnitario), modifier = Modifier.weight(1f))
                    Text(formateaCLP(item.subtotal), modifier = Modifier.weight(1f))
                }
                // --- AÑADIDO ---
                if (!item.mensaje.isNullOrBlank()) {
                    Text(
                        text = "Mensaje: \"${item.mensaje}\"",
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF6D4C41),
                        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                    )
                }
                // ---------------
            }
        }
        Divider()
        // Totales
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text("Subtotal", color = Color.Gray, modifier = Modifier.padding(end = 16.dp))
            Text(formateaCLP(orden.total), fontWeight = FontWeight.Medium)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 16.dp))
            Text(formateaCLP(orden.total), fontWeight = FontWeight.Bold)
        }
    }
}
// -------------------------

@Composable
fun DetalleEnvio(orden: Orden) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Envío", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        InfoEnvioRow("Tipo:", orden.tipoEntrega)
        InfoEnvioRow("Dirección:", orden.direccionEntrega)
        InfoEnvioRow("Fecha preferida:", orden.fechaPreferida)
        InfoEnvioRow("Estado:", orden.estado)
    }
}

@Composable
fun InfoEnvioRow(label: String, value: String) {
    Row {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.width(130.dp))
        Text(value)
    }
}

private fun compartirPdf(context: Context, uri: Uri, pedidoId: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "Boleta Pedido $pedidoId - Pastelería Mil Sabores")
        type = "application/pdf"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartir Boleta PDF")
    context.startActivity(shareIntent)
}