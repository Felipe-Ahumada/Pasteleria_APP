package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel

@Composable
fun AdminScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    onLogout: () -> Unit = {},
    onOpenProductList: () -> Unit = {}, // ✅ NUEVO
    onOpenOrderList: () -> Unit = {}, // ✅ NUEVO
    onOpenUserList: () -> Unit = {}, // ✅ NUEVO
    onOpenReports: () -> Unit = {}, // ✅ NUEVO
    carritoViewModel: CarritoViewModel,
    usuarioViewModel: UsuarioViewModel = hiltViewModel()
) {
    val marron = MaterialTheme.colorScheme.primary
    val crema = MaterialTheme.colorScheme.background

    PasteleriaScaffold(
        title = "Panel de Administración",
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AdminPanelSettings,
                contentDescription = "Admin",
                tint = marron,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "Bienvenido Administrador",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = marron
            )

            Text(
                text = "Selecciona una opción para gestionar:",
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de gestión (Placeholders por ahora)
            AdminOptionCard(
                title = "Gestionar Productos",
                icon = Icons.Default.Inventory,
                color = marron,
                onClick = onOpenProductList // ✅ Conectado
            )

            AdminOptionCard(
                title = "Gestionar Pedidos",
                icon = Icons.Default.ShoppingBag,
                color = marron,
                onClick = onOpenOrderList // ✅ Conectado
            )

            AdminOptionCard(
                title = "Gestionar Usuarios",
                icon = Icons.Default.Person,
                color = marron,
                onClick = onOpenUserList // ✅ Conectado
            )

            AdminOptionCard(
                title = "Reportes",
                icon = Icons.Default.Assessment,
                color = marron,
                onClick = onOpenReports // ✅ Conectado
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    usuarioViewModel.cerrarSesion()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AdminOptionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
    }
}
