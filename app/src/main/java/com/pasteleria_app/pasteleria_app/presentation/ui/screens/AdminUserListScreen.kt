package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.data.model.UserResponse
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.AdminUserViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

@Composable
fun AdminUserListScreen(
    onOpenHome: () -> Unit,
    onOpenNosotros: () -> Unit,
    onOpenCarta: () -> Unit,
    onOpenContacto: () -> Unit,
    onOpenCarrito: () -> Unit,
    onOpenLogin: () -> Unit,
    onOpenPerfil: () -> Unit,
    onBack: () -> Unit,
    carritoViewModel: CarritoViewModel,
    viewModel: AdminUserViewModel = hiltViewModel()
) {
    val usuarios by viewModel.usuarios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedUser by remember { mutableStateOf<UserResponse?>(null) }

    val filteredUsuarios = usuarios.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) ||
        it.correo.contains(searchQuery, ignoreCase = true) ||
        it.run.contains(searchQuery, ignoreCase = true)
    }

    PasteleriaScaffold(
        title = "Gestión de Usuarios",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenLogin = onOpenLogin,
        onOpenPerfil = onOpenPerfil,
        carritoViewModel = carritoViewModel
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with Back button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                    Text(
                        text = "Usuarios",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar por Nombre, Correo o RUN...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (filteredUsuarios.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se encontraron usuarios.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredUsuarios) { user ->
                            UserItemCard(
                                user = user,
                                onClick = { selectedUser = user }
                            )
                        }
                    }
                }
            }
            
            // Error Message
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                )
            }

            // Detail Dialog
            selectedUser?.let { user ->
                AdminUserDetailDialog(
                    user = user,
                    onDismiss = { selectedUser = null },
                    onUpdateUser = { updatedUser ->
                        viewModel.actualizarUsuario(user.id, updatedUser)
                        selectedUser = null
                    },
                    onBlockUser = { userId ->
                        viewModel.bloquearUsuario(userId)
                        selectedUser = null
                    }
                )
            }
        }
    }
}

@Composable
fun UserItemCard(user: UserResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${user.nombre} ${user.apellidos}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = user.correo, fontSize = 12.sp, color = Color.Gray)
                Text(text = "RUN: ${user.run}-${user.dv}", fontSize = 12.sp, color = Color.Gray)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                StatusChip(status = if (user.activo) "Activo" else "Bloqueado")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = user.tipoUsuario.replace("ROLE_", ""), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC1E1C1)), // Greenish
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Ver", fontSize = 12.sp, color = Color.Black)
            }
        }
    }
}
