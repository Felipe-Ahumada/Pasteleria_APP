package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pasteleria_app.pasteleria_app.R
import kotlinx.coroutines.delay

@Composable
fun LandingPage(navController: NavController? = null) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    // ⏳ Efecto de navegación automática después de 3 segundos
    LaunchedEffect(Unit) {
        delay(3000) // 3 segundos
        navController?.navigate("productos") {
            popUpTo("landing") { inclusive = true } // elimina la pantalla de carga del back stack
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = crema
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🧁 Logo
            Image(
                painter = painterResource(id = R.drawable.logo_landing),
                contentDescription = "Logo Pastelería Mil Sabores",
                modifier = Modifier.height(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ✨ Nombre de la pastelería
            Text(
                text = "Pastelería Mil Sabores",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = marron
            )
        }
    }
}
