package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
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
import kotlinx.coroutines.launch
import com.pasteleria_app.pasteleria_app.R

data class Categoria(
    val nombre: String,
    val imagen: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {}
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val categorias = listOf(
        Categoria("Tortas Cuadradas", R.drawable.torta_cuadrada_de_chocolate),
        Categoria("Tortas Circulares", R.drawable.torta_circular_de_vainilla),
        Categoria("Postres Individuales", R.drawable.tiramisu_clasico),
        Categoria("Kuchen y Tartas", R.drawable.cheesecake_sin_azucar)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemClick = { item ->
                    when (item) {
                        "Nosotros" -> {
                            onOpenNosotros()
                            scope.launch { drawerState.close() }
                        }
                        "Carta" -> {
                            onOpenCarta()
                            scope.launch { drawerState.close() }
                        }
                        "Contacto" -> { onOpenContacto(); scope.launch { drawerState.close() } }
                        else -> scope.launch { drawerState.close() }
                    }
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Image(
                            painter = painterResource(id = R.drawable.logo_landing),
                            contentDescription = "Logo Pastelería Mil Sabores",
                            modifier = Modifier.height(45.dp)
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = marron
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = crema
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

                            // 🔸 Botón “Ver productos” ahora lleva a Carta
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
}

@Composable
fun DrawerContent(
    onItemClick: (String) -> Unit,
    onClose: () -> Unit
) {
    val menuItems = listOf(
        "Inicio",
        "Nosotros",
        "Carta",
        "Blog",
        "Contacto",
        "Instagram",
        "Carrito de Compra"
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color(0xFF8B4513))
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🍰 Logo
        Image(
            painter = painterResource(id = R.drawable.logo_landing),
            contentDescription = "Logo Pastelería",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        // 📋 Opciones del menú
        menuItems.forEach { item ->
            TextButton(
                onClick = { onItemClick(item) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Divider(color = Color.White.copy(alpha = 0.3f))

        // 👤 Usuario (por ahora fijo)
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = "admin",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
