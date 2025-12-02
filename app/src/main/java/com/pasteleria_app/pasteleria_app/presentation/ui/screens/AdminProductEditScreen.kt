package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.AdminProductViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductEditScreen(
    productoId: Long,
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    onBack: () -> Unit = {},
    carritoViewModel: CarritoViewModel,
    viewModel: AdminProductViewModel = hiltViewModel()
) {
    val marron = MaterialTheme.colorScheme.primary
    val crema = MaterialTheme.colorScheme.background

    // Estado local para el formulario
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Cargar producto al iniciar
    LaunchedEffect(productoId) {
        val producto = viewModel.obtenerProductoPorId(productoId)
        if (producto != null) {
            nombre = producto.nombre
            precio = producto.precio.toString()
            imagenUrl = producto.imagenUrl ?: ""
            descripcion = producto.descripcion ?: ""
            isLoading = false
        } else {
            error = "No se pudo cargar el producto"
            isLoading = false
        }
    }

    PasteleriaScaffold(
        title = "Editar Producto",
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
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = marron)
                }
                Text(
                    text = "Editar Producto",
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
                // Formulario
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { if (it.all { char -> char.isDigit() }) precio = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de la Imagen") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 5
                )

                Button(
                    onClick = {
                        println("AdminProductEditScreen: Guardando - Nombre: $nombre, Precio: $precio, Desc: $descripcion")
                        viewModel.actualizarProducto(
                            id = productoId,
                            nombre = nombre,
                            precio = precio.toIntOrNull() ?: 0,
                            imagenUrl = imagenUrl,
                            descripcion = descripcion,
                            onSuccess = onBack
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = marron)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
