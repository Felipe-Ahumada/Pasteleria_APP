package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.AdminOrderViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

@Composable
fun AdminOrderListScreen(
    onOpenHome: () -> Unit,
    onOpenNosotros: () -> Unit,
    onOpenCarta: () -> Unit,
    onOpenContacto: () -> Unit,
    onOpenCarrito: () -> Unit,
    onOpenLogin: () -> Unit,
    onOpenPerfil: () -> Unit,
    onBack: () -> Unit,
    carritoViewModel: CarritoViewModel,
    viewModel: AdminOrderViewModel = hiltViewModel()
) {
    val ordenes by viewModel.ordenes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedOrder by remember { mutableStateOf<Orden?>(null) }

    val filteredOrdenes = ordenes.filter {
        it.id.contains(searchQuery, ignoreCase = true) ||
        it.estado.contains(searchQuery, ignoreCase = true)
        // Add more filters if needed
    }

    PasteleriaScaffold(
        title = "Gestión de Pedidos",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenLogin = onOpenLogin,
        onOpenPerfil = onOpenPerfil,
        carritoViewModel = carritoViewModel
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with Back button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                    Text(
                        text = "Pedidos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar por ID o Estado...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (filteredOrdenes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se encontraron pedidos.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredOrdenes) { orden ->
                            OrderItemCard(
                                orden = orden,
                                onClick = { selectedOrder = orden }
                            )
                        }
                    }
                }
            }
            
            // Error Message
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                )
            }

            // Detail Dialog
            selectedOrder?.let { orden ->
                AdminOrderDetailDialog(
                    orden = orden,
                    onDismiss = { selectedOrder = null },
                    onUpdateStatus = { id, status ->
                        viewModel.actualizarEstadoOrden(id, status)
                        selectedOrder = null // Close dialog after update
                    },
                    siguienteEstado = viewModel.getSiguienteEstado(orden.estado)
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(orden: Orden, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Pedido #${orden.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date(orden.fechaCreacion)), fontSize = 12.sp, color = Color.Gray)
                Text(text = "Total: ${formatCurrency(orden.total)}", fontSize = 14.sp)
            }
            
            StatusChip(status = orden.estado)
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC1E1C1)), // Greenish
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Ver", fontSize = 12.sp, color = Color.Black)
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when (status.lowercase()) {
        "pendiente" -> Color(0xFF00BFFF) // Blue
        "preparando" -> Color(0xFFFFA500) // Orange
        "en_camino" -> Color(0xFF1E90FF) // DodgerBlue
        "entregado" -> Color(0xFF32CD32) // LimeGreen
        else -> Color.Gray
    }
    
    Surface(
        color = color,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = formatStatus(status),
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
