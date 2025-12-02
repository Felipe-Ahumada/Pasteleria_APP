package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pasteleria_app.pasteleria_app.domain.model.Producto // ✅ Usar Domain Model
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.AdminProductViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

@Composable
fun AdminProductListScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    onBack: () -> Unit = {},
    onEditProduct: (Long) -> Unit = {}, // ✅ NUEVO
    carritoViewModel: CarritoViewModel,
    viewModel: AdminProductViewModel = hiltViewModel()
) {
    val productos by viewModel.productos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val marron = MaterialTheme.colorScheme.primary
    val crema = MaterialTheme.colorScheme.background

    PasteleriaScaffold(
        title = "Gestión de Productos",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenLogin = onOpenLogin,
        onOpenPerfil = onOpenPerfil,
        carritoViewModel = carritoViewModel
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(crema)
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header con botón volver
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = marron)
                }
                Text(
                    text = "Lista de Productos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = marron
                )
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = marron)
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error ?: "Error desconocido", color = Color.Red)
                }
            } else {
                // Tabla de productos
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos) { producto ->
                        ProductAdminItem(
                            producto = producto,
                            onBlock = { viewModel.bloquearProducto(producto) },
                            onEdit = { onEditProduct(producto.productoId) } // ✅ Conectado
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductAdminItem(
    producto: Producto,
    onBlock: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            AsyncImage(
                model = producto.imagenUrl ?: "https://via.placeholder.com/150",
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                // Domain model no tiene codigoProducto por ahora
                /*Text(
                    text = "Código: ${producto.codigoProducto}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )*/
                Text(
                    text = "$${producto.precio}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                // Estado
                val estado = producto.estado ?: "ACTIVO"
                val estadoColor = if (estado == "ACTIVO") Color(0xFF4CAF50) else Color(0xFFF44336)
                Text(
                    text = estado,
                    color = estadoColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Acciones
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                }
                IconButton(onClick = onBlock) {
                    Icon(
                        imageVector = if (producto.estado == "ACTIVO") Icons.Default.Block else Icons.Default.CheckCircle,
                        contentDescription = if (producto.estado == "ACTIVO") "Bloquear" else "Activar",
                        tint = if (producto.estado == "ACTIVO") Color.Red else Color.Green
                    )
                }
            }
        }
    }
}
