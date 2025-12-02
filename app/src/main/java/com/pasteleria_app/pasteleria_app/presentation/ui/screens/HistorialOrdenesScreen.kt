package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.viewmodel.OrdenViewModel
import com.pasteleria_app.pasteleria_app.utils.BoletaPdfGenerator
import com.pasteleria_app.pasteleria_app.utils.formatTimestamp
import com.pasteleria_app.pasteleria_app.utils.formateaCLP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HistorialOrdenesScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    onOpenAdmin: () -> Unit = {}, // ✅ NUEVO
    onNavigateToDetalle: (String) -> Unit,
    onOpenCarrito: () -> Unit,
    carritoViewModel: CarritoViewModel = hiltViewModel(),
    ordenViewModel: OrdenViewModel = hiltViewModel()
) {
    val ordenes by ordenViewModel.ordenes.collectAsState()
    val crema = Color(0xFFFBF3E9)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Cargar las órdenes cuando la pantalla se inicia
    LaunchedEffect(Unit) {
        ordenViewModel.cargarOrdenes()
    }

    PasteleriaScaffold(
        title = "Mis pedidos",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenLogin = onOpenLogin,
        onOpenPerfil = onOpenPerfil,
        onOpenCarrito = onOpenCarrito,
        onOpenAdmin = onOpenAdmin,
        carritoViewModel = carritoViewModel
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(crema)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Historial",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E2E20)
                )
            }

            if (ordenes.isEmpty()) {
                item {
                    Text(
                        "Aún no tienes pedidos.",
                        modifier = Modifier.padding(top = 20.dp),
                        fontSize = 16.sp
                    )
                }
            } else {
                items(ordenes) { orden ->
                    OrdenHistorialItem(
                        orden = orden,
                        onVerSeguimiento = { onNavigateToDetalle(orden.id) },
                        // --- LÓGICA DE PDF AÑADIDA ---
                        onImprimirBoleta = {
                            scope.launch {
                                try {
                                    // 1. Generar PDF en hilo de I/O
                                    val pdfUri = withContext(Dispatchers.IO) {
                                        BoletaPdfGenerator.generar(context, orden)
                                    }
                                    // 2. Compartir en hilo principal
                                    compartirPdf(context, pdfUri, orden.id)
                                } catch (e: android.content.ActivityNotFoundException) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "No se encontró una app para abrir el PDF", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    // ---- CORREGIDO ----
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        // ------------------------------
                    )
                }
            }
        }
    }
}

@Composable
fun OrdenHistorialItem(
    orden: Orden,
    onVerSeguimiento: () -> Unit,
    onImprimirBoleta: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Pedido", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(orden.id, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Tracking: ${orden.trackingId}", fontSize = 14.sp)
            Text("Creado: ${formatTimestamp(orden.fechaCreacion)}", fontSize = 14.sp, color = Color.Gray)

            Spacer(Modifier.height(8.dp))

            Text("Estado", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            // Chip de estado (similar a tu imagen)
            Box(
                modifier = Modifier
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(orden.estado, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(8.dp))

            Text("Total", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(formateaCLP(orden.total), fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = onVerSeguimiento,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Ver seguimiento")
            }
            Button(
                onClick = onImprimirBoleta, // Este onClick ahora llama a la corrutina
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5), contentColor = Color.Black),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Imprimir boleta")
            }
        }
    }
}


/**
 * Lanza el "Share Sheet" de Android para compartir un archivo PDF.
 */
private fun compartirPdf(context: Context, uri: Uri, pedidoId: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "Boleta Pedido $pedidoId - Pastelería Mil Sabores")
        type = "application/pdf"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Muy importante
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartir Boleta PDF")
    context.startActivity(shareIntent)
}