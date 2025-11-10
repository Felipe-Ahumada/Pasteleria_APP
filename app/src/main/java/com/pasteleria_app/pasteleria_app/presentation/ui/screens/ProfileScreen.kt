package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onLogout: () -> Unit = {},
    carritoViewModel: CarritoViewModel = hiltViewModel() // ✅ agregado aquí
) {
    val usuarioViewModel: UsuarioViewModel = hiltViewModel()
    val nombreUsuario by usuarioViewModel.usuarioActual.collectAsState(initial = "")
    val correoUsuario by usuarioViewModel.usuarioCorreo.collectAsState(initial = "")
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()

    PasteleriaScaffold(
        title = "Mi Perfil",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        carritoViewModel = carritoViewModel // ✅ se pasa aquí
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(crema)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Mi Perfil",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = marron
                    )

                    // 📷 Imagen o inicial del usuario
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0D8C6)),
                        contentAlignment = Alignment.Center
                    ) {
                        val inicial = nombreUsuario?.firstOrNull()?.uppercaseChar()?.toString()

                        if (!inicial.isNullOrEmpty()) {
                            Text(
                                text = inicial,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = marron
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Foto de perfil",
                                tint = Color(0xFF8B4513),
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }

                    // Botones cambiar y quitar foto (futuros)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { /* TODO: cambiar foto */ },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cambiar foto")
                        }
                        OutlinedButton(
                            onClick = { /* TODO: eliminar foto */ },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Quitar foto", color = Color.Red)
                        }
                    }

                    Divider(thickness = 1.dp, color = Color.LightGray)

                    // Datos del usuario
                    Text("Nombre: $nombreUsuario", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text("Correo: $correoUsuario", fontSize = 16.sp, color = Color.DarkGray)

                    // Botón "Mis pedidos"
                    OutlinedButton(
                        onClick = { /* TODO: ver pedidos */ },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text("Mis pedidos", fontWeight = FontWeight.Bold)
                    }

                    // Botón "Cerrar sesión"
                    Button(
                        onClick = {
                            scope.launch {
                                usuarioViewModel.cerrarSesion()
                                onLogout()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
