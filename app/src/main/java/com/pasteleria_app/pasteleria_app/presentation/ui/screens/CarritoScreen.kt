package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun CarritoScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    carritoViewModel: CarritoViewModel
) {
    val productos = carritoViewModel.productos
    val total = carritoViewModel.calcularTotal()
    val crema = Color(0xFFF8EFE5)
    val marron = Color(0xFF4E342E)

    PasteleriaScaffold(
        title = "Carrito de compras",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(crema)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 🧾 Encabezado de columnas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Producto", fontWeight = FontWeight.Bold, color = marron)
                Text("Cantidad", fontWeight = FontWeight.Bold, color = marron)
                Text("Precio", fontWeight = FontWeight.Bold, color = marron)
            }

            Divider(thickness = 1.dp, color = Color(0xFFD7CCC8))

            // 🍰 Lista de productos
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tu carrito está vacío",
                            fontSize = 18.sp,
                            color = marron,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(
                            onClick = onOpenCarta,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC49A6C)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Ver productos",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productos) { producto ->
                        CarritoItem(
                            nombre = producto.nombre,
                            precio = producto.precio,
                            imagen = producto.imagen,
                            cantidad = producto.cantidad,
                            onAumentar = { carritoViewModel.aumentarCantidad(producto) },
                            onDisminuir = { carritoViewModel.disminuirCantidad(producto) },
                            onEliminar = { carritoViewModel.eliminarProducto(producto) }
                        )
                    }
                }

                // 🧼 Vaciar carrito
                Button(
                    onClick = { carritoViewModel.vaciarCarrito() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = ButtonDefaults.outlinedButtonBorder,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Text("Vaciar carrito", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }

                // 💰 Resumen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Resumen", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", color = Color.Gray)
                            Text(formatearPrecio(total))
                        }
                        Text("Sin descuentos aplicados", color = Color.Gray, fontSize = 13.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Envío", color = Color.Gray)
                            Text("—")
                        }

                        Divider(color = Color(0xFFE0D8C6), thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold)
                            Text(formatearPrecio(total), fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { /* TODO: Continuar */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC49A6C)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Definir envío y continuar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarritoItem(
    nombre: String,
    precio: Int,
    imagen: Int,
    cantidad: Int,
    onAumentar: () -> Unit,
    onDisminuir: () -> Unit,
    onEliminar: () -> Unit
) {
    val marron = Color(0xFF4E342E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = nombre,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(nombre, fontWeight = FontWeight.Bold, color = marron)
            Text("Código: TORTA", color = Color.Gray, fontSize = 13.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onDisminuir,
                modifier = Modifier.size(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5E9D3)),
                contentPadding = PaddingValues(0.dp)
            ) { Text("-", color = marron, fontWeight = FontWeight.Bold, fontSize = 16.sp) }

            Text(
                cantidad.toString(),
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onAumentar,
                modifier = Modifier.size(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5E9D3)),
                contentPadding = PaddingValues(0.dp)
            ) { Text("+", color = marron, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(formatearPrecio(precio), color = marron, fontWeight = FontWeight.Bold)
            Button(
                onClick = onEliminar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                border = ButtonDefaults.outlinedButtonBorder,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("Eliminar", color = Color(0xFFD32F2F), fontSize = 13.sp)
            }
        }
    }
}

// 💵 Formatear precios con puntos (estilo chileno)
fun formatearPrecio(valor: Int): String {
    val formato = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$" + formato.format(valor)
}
