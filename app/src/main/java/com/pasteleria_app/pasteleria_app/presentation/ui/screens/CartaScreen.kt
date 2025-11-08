package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.pasteleria_app.pasteleria_app.R
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ProductoCarta(
    val nombre: String,
    val precio: String,
    val imagen: Int
)

@Composable
fun CartaScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    carritoViewModel: CarritoViewModel
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    val productos = listOf(
        ProductoCarta("Torta Cuadrada de Chocolate", "$45.000", R.drawable.torta_cuadrada_de_chocolate),
        ProductoCarta("Torta Circular de Vainilla", "$40.000", R.drawable.torta_circular_de_vainilla),
        ProductoCarta("Tiramisú Clásico", "$5.500", R.drawable.tiramisu_clasico),
        ProductoCarta("Cheesecake Sin Azúcar", "$47.000", R.drawable.cheesecake_sin_azucar)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    PasteleriaScaffold(
        title = "Nuestra Carta",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        carritoViewModel = carritoViewModel
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .background(crema)
                    .padding(padding),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos) { producto ->
                    ProductoCard(producto, marron) {
                        // ✅ Crear un Producto (Room model) a partir de ProductoCarta
                        val productoEntity = Producto(
                            id = 0, // Room lo autogenera
                            nombre = producto.nombre,
                            precio = producto.precio.replace("$", "").replace(".", "").toInt(),
                            imagen = producto.imagen,
                            cantidad = 1
                        )

                        carritoViewModel.agregarProducto(productoEntity)

                        scope.launch {
                            snackbarHostState.showSnackbar("Producto agregado al carrito 🍰")
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) { snackbarData ->
                Snackbar(
                    snackbarData,
                    containerColor = Color(0xFFF5E9D3),
                    contentColor = Color(0xFF3E2E20),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
fun ProductoCard(producto: ProductoCarta, marron: Color, onAddToCart: () -> Unit) {
    var agregado by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2E20),
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = producto.precio,
                fontSize = 15.sp,
                color = Color(0xFF3E2E20),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Button(
                onClick = { /* TODO: Ver detalle */ },
                colors = ButtonDefaults.buttonColors(containerColor = marron),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .heightIn(min = 55.dp)
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    "Ver detalle y personalizar",
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }

            Button(
                onClick = {
                    onAddToCart()
                    agregado = true
                    scope.launch {
                        delay(2000)
                        agregado = false
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (agregado) Color(0xFFF5E9D3) else marron
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .heightIn(min = 50.dp)
            ) {
                Text(
                    if (agregado) "Agregado ✅" else "Añadir al carrito",
                    fontSize = 13.sp,
                    color = if (agregado) Color(0xFF3E2E20) else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
