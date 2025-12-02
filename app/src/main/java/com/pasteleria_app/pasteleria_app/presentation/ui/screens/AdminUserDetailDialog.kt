package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pasteleria_app.pasteleria_app.data.model.User
import com.pasteleria_app.pasteleria_app.data.model.UserResponse

@Composable
fun AdminUserDetailDialog(
    user: UserResponse,
    onDismiss: () -> Unit,
    onUpdateUser: (User) -> Unit,
    onBlockUser: (Long) -> Unit
) {
    val isSuperAdmin = user.tipoUsuario == "ROLE_SUPERADMIN"
    
    // Editable state
    var nombre by remember { mutableStateOf(user.nombre) }
    var apellidos by remember { mutableStateOf(user.apellidos) }
    var correo by remember { mutableStateOf(user.correo) }
    // Add other editable fields as needed

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSuperAdmin) "Detalle de Usuario (Solo Lectura)" else "Editar Usuario",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Divider(color = Color.LightGray, thickness = 1.dp)

                // Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ID & RUN (Read-only)
                    DetailRow("ID:", user.id.toString())
                    DetailRow("RUN:", "${user.run}-${user.dv}")
                    
                    if (isSuperAdmin) {
                        DetailRow("Nombre:", user.nombre)
                        DetailRow("Apellidos:", user.apellidos)
                        DetailRow("Correo:", user.correo)
                    } else {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = apellidos,
                            onValueChange = { apellidos = it },
                            label = { Text("Apellidos") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    DetailRow("Tipo Usuario:", user.tipoUsuario)
                    DetailRow("Estado:", if (user.activo) "Activo" else "Bloqueado")
                    DetailRow("Región:", user.regionNombre ?: "-")
                    DetailRow("Comuna:", user.comuna ?: "-")
                    DetailRow("Dirección:", user.direccion ?: "-")
                }

                // Footer Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC0CB))
                    ) {
                        Text("Cerrar", color = Color.Black)
                    }

                    if (!isSuperAdmin) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onBlockUser(user.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = if (user.activo) Color.Red else Color.Gray)
                            ) {
                                Text(if (user.activo) "Bloquear" else "Desbloquear", color = Color.White)
                            }
                            
                            Button(
                                onClick = {
                                    // Construct User object for update
                                    val updatedUser = User(
                                        id = user.id,
                                        run = user.run,
                                        dv = user.dv,
                                        nombre = nombre,
                                        apellidos = apellidos,
                                        correo = correo,
                                        fechaNacimiento = user.fechaNacimiento,
                                        codigoDescuento = user.codigoDescuento,
                                        tipoUsuario = user.tipoUsuario,
                                        regionId = user.regionId,
                                        regionNombre = user.regionNombre,
                                        comuna = user.comuna,
                                        direccion = user.direccion,
                                        avatarUrl = user.avatarUrl,
                                        activo = user.activo
                                    )
                                    onUpdateUser(updatedUser)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Guardar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
