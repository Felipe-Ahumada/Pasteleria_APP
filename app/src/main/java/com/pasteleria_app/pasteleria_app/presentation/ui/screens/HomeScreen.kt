package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.Image
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
import com.pasteleria_app.pasteleria_app.R
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

data class Categoria(
    val nombre: String,
    val imagen: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    carritoViewModel: CarritoViewModel // ✅ añadimos ViewModel para mostrar badge
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    val categorias = listOf(
        Categoria("Tortas Cuadradas", R.drawable.torta_cuadrada_de_chocolate),
        Categoria("Tortas Circulares", R.drawable.torta_circular_de_vainilla),
        Categoria("Postres Individuales", R.drawable.tiramisu_clasico),
        Categoria("Kuchen y Tartas", R.drawable.cheesecake_sin_azucar)
    )

    // ✅ Usa el mismo scaffold que las demás pantallas
    PasteleriaScaffold(
        title = "Pastelería 1000 Sabores",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        carritoViewModel = carritoViewModel // ✅ activa la badge
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 🌸 Imagen principal con botón “Conócenos”
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.carrusel_carta),
                        contentDescription = "Imagen principal de la pastelería",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Button(
                        onClick = { onOpenNosotros() },
                        colors = ButtonDefaults.buttonColors(containerColor = crema),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 24.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Conócenos",
                            fontSize = 18.sp,
                            color = marron,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 🧁 Título “Nuestra Carta”
            item {
                Text(
                    text = "Nuestra Carta",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = marron,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            // 🍰 Tarjetas de categorías
            items(categorias) { categoria ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = categoria.imagen),
                            contentDescription = categoria.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = categoria.nombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3E2E20),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // 🔸 Botón “Ver productos” lleva a Carta
                        Button(
                            onClick = { onOpenCarta() },
                            colors = ButtonDefaults.buttonColors(containerColor = marron),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(55.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Ver productos",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
