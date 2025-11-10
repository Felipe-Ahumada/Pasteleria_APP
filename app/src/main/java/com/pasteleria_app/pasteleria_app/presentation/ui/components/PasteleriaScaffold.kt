package com.pasteleria_app.pasteleria_app.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pasteleria_app.pasteleria_app.R
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.ui.text.font.FontWeight
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasteleriaScaffold(
    title: String,
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {}, // 👈 nuevo
    carritoViewModel: CarritoViewModel? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ✅ Observa los productos del carrito en tiempo real
    val productos by carritoViewModel?.productos?.collectAsState() ?: remember { mutableStateOf(emptyList()) }

    // ✅ Calcula el total de productos (cantidad total de items)
    val totalProductos = productos.sumOf { it.cantidad }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemClick = { item ->
                    when (item) {
                        "Inicio" -> { onOpenHome(); scope.launch { drawerState.close() } }
                        "Nosotros" -> { onOpenNosotros(); scope.launch { drawerState.close() } }
                        "Carta" -> { onOpenCarta(); scope.launch { drawerState.close() } }
                        "Contacto" -> { onOpenContacto(); scope.launch { drawerState.close() } }
                        "Carrito de Compra" -> { onOpenCarrito(); scope.launch { drawerState.close() } }
                        "Login" -> { onOpenLogin(); scope.launch { drawerState.close() } }
                        "Perfil" -> { onOpenPerfil(); scope.launch { drawerState.close() } } // ✅ añadido
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
                        Text(
                            text = title,
                            color = marron,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        Box(contentAlignment = Alignment.TopEnd) {
                            IconButton(onClick = onOpenCarrito) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito",
                                    tint = marron
                                )
                            }

                            // 🎯 Muestra badge si hay productos
                            if (totalProductos > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .offset(x = (-6).dp, y = 6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFD32F2F)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = totalProductos.toString(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
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
            containerColor = crema,
            content = content
        )
    }
}

@Composable
fun DrawerContent(
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    usuarioViewModel: UsuarioViewModel = hiltViewModel() // ✅ agregamos esto
) {
    val nombreUsuario by usuarioViewModel.usuarioActual.collectAsState(initial = null)
    val menuItems = listOf("Inicio", "Nosotros", "Carta", "Contacto", "Carrito de Compra")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color(0xFF8B4513))
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_landing),
            contentDescription = "Logo Pastelería",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 16.dp)
        )

        menuItems.forEach { item ->
            TextButton(onClick = { onItemClick(item) }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Divider(
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        if (nombreUsuario != null) {
            // 👤 Usuario logueado
            TextButton(
                onClick = { onItemClick("Perfil") }, // 🔜 redirige al perfil
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = nombreUsuario ?: "Usuario",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // 🚪 No hay sesión
            TextButton(
                onClick = { onItemClick("Login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Iniciar sesión",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Iniciar sesión",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
