package com.pasteleria_app.pasteleria_app.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasteleriaScaffold(
    title: String,
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                        Text(
                            text = title,
                            color = Color(0xFF3E2E20),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color(0xFF3E2E20)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}

@Composable
private fun DrawerContent(
    onItemClick: (String) -> Unit,
    onClose: () -> Unit
) {
    val menuItems = listOf(
        "Inicio",
        "Nosotros",
        "Carta",
        "Contacto",
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
                .clip(RoundedCornerShape(8.dp))
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
