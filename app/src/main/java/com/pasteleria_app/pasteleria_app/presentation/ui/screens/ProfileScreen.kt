package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onLogout: () -> Unit = {},
    onOpenHistorial: () -> Unit = {}, // <-- Ya está aquí
    onOpenAdmin: () -> Unit = {}, // ✅ NUEVO
    carritoViewModel: CarritoViewModel = hiltViewModel()
) {
    val usuarioViewModel: UsuarioViewModel = hiltViewModel()
    val nombreUsuario by usuarioViewModel.usuarioActual.collectAsState(initial = "")
    val correoUsuario by usuarioViewModel.usuarioCorreo.collectAsState(initial = "")
    val fotoGuardada by usuarioViewModel.usuarioFoto.collectAsState(initial = null)

    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var fotoPerfil by remember { mutableStateOf<Uri?>(null) }

    // 🔁 Actualiza foto al cambiar en DataStore
    LaunchedEffect(fotoGuardada) {
        fotoPerfil = fotoGuardada?.let { Uri.parse(it) }
    }

    var mostrarDialogo by remember { mutableStateOf(false) }

    // Archivo persistente
    val fotoDir = remember { File(context.filesDir, "profile_photos").apply { mkdirs() } }
    val archivoFoto = remember { File(fotoDir, "profile_${System.currentTimeMillis()}.jpg") }
    val uriFoto = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        archivoFoto
    )

    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            fotoPerfil = uriFoto
            usuarioViewModel.guardarFotoPerfil(uriFoto.toString())
        }
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            fotoPerfil = uri
            usuarioViewModel.guardarFotoPerfil(uri.toString())
        }
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permisoConcedido ->
        if (permisoConcedido) camaraLauncher.launch(uriFoto)
        else Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    PasteleriaScaffold(
        title = "Mi Perfil",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenAdmin = onOpenAdmin, // ✅ NUEVO
        carritoViewModel = carritoViewModel
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
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Mi Perfil",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = marron
                    )

                    // 🖼️ Imagen o inicial
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0D8C6)),
                        contentAlignment = Alignment.Center
                    ) {
                        val nombreActual = nombreUsuario ?: ""

                        when {
                            fotoPerfil != null -> Image(
                                painter = rememberAsyncImagePainter(fotoPerfil),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            nombreActual.isNotEmpty() -> Text(
                                text = nombreActual.first().uppercaseChar().toString(),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = marron
                            )

                            else -> Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = null,
                                tint = marron,
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { mostrarDialogo = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = marron)
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Cambiar foto de perfil", fontWeight = FontWeight.Bold)
                    }

                    Divider(thickness = 1.dp, color = Color.LightGray)

                    Text("Nombre: $nombreUsuario", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    Text("Correo: $correoUsuario", fontSize = 16.sp, color = Color.DarkGray)

                    // ---- MODIFICADO ----
                    OutlinedButton(
                        onClick = onOpenHistorial, // <-- Se usa el parámetro
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text("Mis pedidos", fontWeight = FontWeight.Bold)
                    }
                    // --------------------

                    Button(
                        onClick = {
                            scope.launch {
                                val correoActual = usuarioViewModel.usuarioCorreo.firstOrNull() ?: ""
                                if (correoActual.isNotBlank())
                                    carritoViewModel.guardarCarritoUsuario(correoActual)
                                carritoViewModel.vaciarCarrito()
                                usuarioViewModel.cerrarSesion()
                                onLogout()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.7f).height(50.dp)
                    ) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {},
            title = { Text("Cambiar foto de perfil") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            mostrarDialogo = false
                            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = marron),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Tomar una foto")
                    }
                    OutlinedButton(
                        onClick = {
                            mostrarDialogo = false
                            galeriaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Photo, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Elegir desde galería")
                    }
                }
            }
        )
    }
}