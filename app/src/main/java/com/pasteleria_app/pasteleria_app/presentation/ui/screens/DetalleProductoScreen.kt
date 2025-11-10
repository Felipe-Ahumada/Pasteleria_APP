package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.R
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// Esta lista es un "espejo" de la que está en CartaScreen.
// Es necesario para encontrar el producto basado en el nombre que recibimos.
private val productos = listOf(
    ProductoCarta("Torta Cuadrada de Chocolate", "$45.000", R.drawable.torta_cuadrada_de_chocolate),
    ProductoCarta("Torta Circular de Vainilla", "$40.000", R.drawable.torta_circular_de_vainilla),
    ProductoCarta("Tiramisú Clásico", "$5.500", R.drawable.tiramisu_clasico),
    ProductoCarta("Cheesecake Sin Azúcar", "$47.000", R.drawable.cheesecake_sin_azucar)
)

// Mapa de descripciones (ya que no están en el modelo)
private val descripciones = mapOf(
    "Torta Cuadrada de Chocolate" to "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales."
    // ... (añadir otras descripciones si es necesario)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    nombreProductoUrl: String, // Recibimos el nombre desde la URL
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    carritoViewModel: CarritoViewModel = hiltViewModel()
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Decodificar el nombre del producto (por si tiene espacios)
    val nombreProducto = remember {
        try {
            URLDecoder.decode(nombreProductoUrl, StandardCharsets.UTF_8.name())
        } catch (e: Exception) {
            nombreProductoUrl // Fallback
        }
    }

    // Encontrar el producto en nuestra lista "espejo"
    val producto = remember { productos.firstOrNull { it.nombre == nombreProducto } }

    var cantidad by remember { mutableStateOf(1) }
    var mensaje by remember { mutableStateOf("") }

    if (producto == null) {
        // Fallback si no se encuentra el producto
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado")
        }
        return
    }

    PasteleriaScaffold(
        title = producto.nombre, // Título de la página
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenLogin = onOpenLogin,
        onOpenPerfil = onOpenPerfil,
        carritoViewModel = carritoViewModel
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(crema)
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Breadcrumbs (migas de pan)
                item {
                    Text(
                        text = "Inicio / Carta / ${producto.nombre}",
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                // Imagen principal
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = producto.imagen),
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Miniaturas (simuladas como en la imagen)
                item {
                    Row(
                        Modifier.padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = producto.imagen),
                            contentDescription = "miniatura 1",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(2.dp, marron, RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = producto.imagen),
                            contentDescription = "miniatura 2",
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = producto.imagen),
                            contentDescription = "miniatura 3",
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Nombre, Código y Precio
                item {
                    Text(
                        text = producto.nombre,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = marron,
                        textAlign = TextAlign.Center
                    )
                    Text("Código: TC001", fontSize = 14.sp, color = Color.Gray) // Código hardcodeado
                    Text(
                        text = producto.precio,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = marron,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Descripción
                item {
                    Text(
                        text = descripciones[producto.nombre] ?: "Deliciosa torta. Personalizable con mensajes especiales.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }

                // Selector de Cantidad
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { if (cantidad > 1) cantidad-- },
                            modifier = Modifier.size(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) { Text("–", fontSize = 24.sp) }

                        Text(
                            text = "$cantidad",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.widthIn(min = 40.dp),
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = { cantidad++ },
                            modifier = Modifier.size(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) { Text("+", fontSize = 24.sp) }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                // Mensaje en la Torta
                item {
                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = { if (it.length <= 50) mensaje = it },
                        label = { Text("Mensaje en la torta (opcional)") },
                        placeholder = { Text("Ej.: ¡Feliz Cumple, Vale!") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        trailingIcon = {
                            Text(
                                "${mensaje.length}/50",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 12.sp
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Botón Añadir al carrito
                item {
                    Button(
                        onClick = {
                            // Crear el objeto Producto
                            val productoEntity = Producto(
                                nombre = producto.nombre,
                                precio = producto.precio.replace("$", "").replace(".", "").toInt(),
                                imagen = producto.imagen,
                                cantidad = cantidad,
                                mensaje = mensaje.takeIf { it.isNotBlank() } // Añadir mensaje
                            )
                            carritoViewModel.agregarProducto(productoEntity)
                            scope.launch {
                                snackbarHostState.showSnackbar("¡Agregado al carrito! 🍰")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Añadir al carrito", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                }

                // Botón Compartir
                item {
                    OutlinedButton(
                        onClick = {
                            compartirProducto(context, producto.nombre, producto.precio)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Compartir", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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

private fun compartirProducto(context: Context, nombre: String, precio: String) {
    val texto = "¡Mira este producto de Pastelería Mil Sabores!\n\n$nombre - $precio\n¡Te va a encantar!"
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, texto)
        putExtra(Intent.EXTRA_SUBJECT, "Mira este producto: $nombre")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartir Producto")
    context.startActivity(shareIntent)
}