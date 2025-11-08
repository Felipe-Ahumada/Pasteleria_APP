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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.ProductoCarrito

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

    PasteleriaScaffold(
        title = "Carrito de compras",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        carritoViewModel = carritoViewModel
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFBF3E9))
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Encabezado
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Producto", fontWeight = FontWeight.Bold, color = Color(0xFF3E2E20))
                    Text("Cantidad", fontWeight = FontWeight.Bold, color = Color(0xFF3E2E20))
                    Text("Precio", fontWeight = FontWeight.Bold, color = Color(0xFF3E2E20))
                }
                Divider(color = Color(0xFFE0D8C6), thickness = 1.dp)
            }

            // Productos
            items(productos) { producto ->
                CarritoItem(
                    producto = producto,
                    onIncrement = { carritoViewModel.aumentarCantidad(producto) },
                    onDecrement = { carritoViewModel.disminuirCantidad(producto) },
                    onRemove = { carritoViewModel.eliminarProducto(producto) }
                )
                Divider(color = Color(0xFFE0D8C6), thickness = 1.dp)
            }

            // Vaciar carrito
            if (productos.isNotEmpty()) {
                item {
                    OutlinedButton(
                        onClick = { carritoViewModel.vaciarCarrito() },
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Text("Vaciar carrito", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Resumen
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Resumen", fontWeight = FontWeight.Bold, fontSize = 20.sp)

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", color = Color.Gray)
                            Text(formateaCLP(total), fontWeight = FontWeight.Medium)
                        }

                        Text("Sin descuentos aplicados", color = Color.Gray, fontSize = 13.sp)

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Envío", color = Color.Gray)
                            Text("—", color = Color.Gray)
                        }

                        Divider(color = Color(0xFFE0D8C6))

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold)
                            Text(formateaCLP(total), fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { /* TODO: Checkout */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC49A6C)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text("Definir envío y continuar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun CarritoItem(
    producto: ProductoCarrito,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Imagen + nombre
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1.5f)
        ) {
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(10.dp))

            Column {
                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = codigoDesdeNombre(producto.nombre),
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }

        // Cantidad
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onDecrement,
                border = ButtonDefaults.outlinedButtonBorder,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Text("–", fontSize = 18.sp, color = Color(0xFF3E2E20))
            }

            Text(
                "${producto.cantidad}",
                modifier = Modifier.padding(horizontal = 10.dp),
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = onIncrement,
                border = ButtonDefaults.outlinedButtonBorder,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(36.dp)
            ) {
                Text("+", fontSize = 18.sp, color = Color(0xFF3E2E20))
            }
        }

        // Precio + eliminar
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(formateaCLP(producto.precio), fontWeight = FontWeight.Bold, fontSize = 16.sp)

            OutlinedButton(
                onClick = onRemove,
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Text("Eliminar", color = Color(0xFFD32F2F), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// $45.000
private fun formateaCLP(valor: Int): String =
    "$" + "%,d".format(valor).replace(',', '.')

// TC001, PI002, etc.
private fun codigoDesdeNombre(nombre: String): String {
    val base = nombre.take(2).uppercase().replace(" ", "")
    val tipo = when {
        nombre.contains("Torta", true) -> "TC"
        nombre.contains("Pie", true) -> "PI"
        else -> "PR"
    }
    val num = (100..999).random()
    return "$tipo${num}"
}
