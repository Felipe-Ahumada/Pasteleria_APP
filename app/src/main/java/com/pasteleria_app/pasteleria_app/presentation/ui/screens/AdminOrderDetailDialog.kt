package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdminOrderDetailDialog(
    orden: Orden,
    onDismiss: () -> Unit,
    onUpdateStatus: (String, String) -> Unit, // ordenId, nuevoEstado
    siguienteEstado: String?
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false) // Full width/custom width
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pedido #${orden.id}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Divider(color = Color.LightGray, thickness = 1.dp)

                // Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Estado y Fecha
                    DetailRow("Estado actual:", orden.estado)
                    DetailRow("Fecha:", java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(java.util.Date(orden.fechaCreacion)))
                    DetailRow("Total:", formatCurrency(orden.total))

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Datos del Cliente", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    // Aquí idealmente mostraríamos nombre y correo, pero el modelo Orden actual 
                    // tiene direccionEntrega y comuna. El nombre del cliente no está directamente en Orden 
                    // a menos que hagamos un join o lo traigamos. 
                    // Por ahora mostraremos lo que tenemos.
                    DetailRow("Dirección:", orden.direccionEntrega)
                    DetailRow("Comuna:", orden.comuna)
                    DetailRow("Tipo Entrega:", orden.tipoEntrega)
                    DetailRow("Fecha Preferida:", orden.fechaPreferida)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Productos", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    orden.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.cantidad}x ${item.nombreProducto}", modifier = Modifier.weight(1f))
                            Text(formatCurrency(item.subtotal))
                        }
                        item.mensaje?.let { mensaje ->
                            if (mensaje.isNotEmpty()) {
                                Text("Nota: $mensaje", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp))
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }

                // Footer Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB)) // Pinkish
                    ) {
                        Text("Cerrar", color = Color.Black)
                    }

                    siguienteEstado?.let { nextStatus ->
                        Button(
                            onClick = { onUpdateStatus(orden.id, nextStatus) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC1E1C1)) // Greenish
                        ) {
                            Text("Avanzar a \"${formatStatus(nextStatus)}\"", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(120.dp))
        Text(text = value, modifier = Modifier.weight(1f))
    }
}

fun formatCurrency(amount: Int): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(amount)
}

fun formatStatus(status: String): String {
    return status.replace("_", " ").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
