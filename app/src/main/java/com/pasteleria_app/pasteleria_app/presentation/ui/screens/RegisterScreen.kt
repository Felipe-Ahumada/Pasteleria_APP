package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold

@Composable
fun RegisterScreen(
    onOpenHome: () -> Unit = {},
    onOpenLogin: () -> Unit = {}
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    var nombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var run by remember { mutableStateOf("") }
    var nacimiento by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var codigoBienvenida by remember { mutableStateOf("") }
    var aceptaTerminos by remember { mutableStateOf(false) }

    PasteleriaScaffold(
        title = "Crear cuenta",
        onOpenHome = onOpenHome
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(crema)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "Crear cuenta",
                tint = marron,
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = "Crear cuenta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = marron,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campos
            campo("Primer nombre", "Ej: María", nombre) { nombre = it }
            campo("Segundo nombre (opcional)", "Ej: Luisa", segundoNombre) { segundoNombre = it }
            campo("Apellido paterno", "Ej: Pérez", apellidoPaterno) { apellidoPaterno = it }
            campo("Apellido materno (opcional)", "Ej: González", apellidoMaterno) { apellidoMaterno = it }
            campo("RUN", "19011022K", run) { run = it }
            campo("Fecha de nacimiento (opcional)", "dd/mm/aaaa", nacimiento) { nacimiento = it }
            campo("Teléfono (opcional)", "+56 9 1234 5678", telefono) { telefono = it }
            campo("Correo electrónico", "usuario@dominio.com", correo) { correo = it }
            campo("Dirección", "Calle 123", direccion) { direccion = it }
            campo("Región", "Selecciona una región", region) { region = it }
            campo("Comuna", "Selecciona una comuna", comuna) { comuna = it }

            // Contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "La contraseña debe tener entre 4 a 10 caracteres.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )

            // Confirmar contraseña
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            campo("Código de bienvenida (opcional)", "Ej: MILSABORES2025", codigoBienvenida) {
                codigoBienvenida = it
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = { aceptaTerminos = it },
                    colors = CheckboxDefaults.colors(checkedColor = marron)
                )
                Text(
                    text = "Acepto los términos y condiciones",
                    color = marron,
                    modifier = Modifier.clickable { aceptaTerminos = !aceptaTerminos }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    // TODO: lógica para crear cuenta
                },
                colors = ButtonDefaults.buttonColors(containerColor = marron),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Crear cuenta", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("¿Ya tienes cuenta?", color = Color.DarkGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Inicia sesión",
                    color = marron,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onOpenLogin() }
                )
            }
        }
    }
}

// ✅ Campo reutilizable
@Composable
fun campo(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
