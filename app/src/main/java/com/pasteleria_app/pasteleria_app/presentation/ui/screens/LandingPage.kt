package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.R
import kotlinx.coroutines.delay

@Composable
fun LandingPage(onEnterClick: () -> Unit) {
    val marron = Color(0xFF8B4513)

    // ⏳ Transición automática después de unos segundos
    LaunchedEffect(Unit) {
        delay(3000)
        onEnterClick()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 🌄 Fondo con imagen difuminada
        Image(
            painter = painterResource(id = R.drawable.carrusel_carta),
            contentDescription = "Fondo Pastelería",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🟤 Degradado oscuro en la parte inferior para mejorar contraste del texto
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xAA000000)
                        ),
                        startY = 400f
                    )
                )
        )

        // 🧁 Contenido centrado
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            // 🍰 Logo circular con sombra
            Image(
                painter = painterResource(id = R.drawable.logo_landing),
                contentDescription = "Logo Pastelería Mil Sabores",
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .shadow(8.dp, CircleShape)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ✨ Texto con mejor visibilidad
            Text(
                text = "50 años endulzando momentos\ninolvidables",
                color = Color(0xFFF5E9DC), // crema clara
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
