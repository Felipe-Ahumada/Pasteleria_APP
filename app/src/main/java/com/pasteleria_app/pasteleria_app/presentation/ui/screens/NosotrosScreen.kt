package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.R
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

@Composable
fun NosotrosScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {},
    onOpenLogin: () -> Unit = {},
    onOpenPerfil: () -> Unit = {},
    carritoViewModel: CarritoViewModel // 👈 agregado para el badge del carrito
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    PasteleriaScaffold(
        title = "Nosotros",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito,
        onOpenLogin = { onOpenLogin() },
        onOpenPerfil = onOpenPerfil,
        carritoViewModel = carritoViewModel
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(crema)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔸 Sección "Quiénes Somos"
            SectionNosotros(
                imagen = R.drawable.vista_pasteleria_mil_sabores,
                titulo = "Quiénes Somos",
                descripcion = """
                    En Pastelería 1000 Sabores celebramos 50 años de historia endulzando momentos únicos y siendo un referente de la repostería chilena. 
                    Desde nuestro inolvidable récord Guinness en 1995, cuando colaboramos en la creación de la torta más grande del mundo, hemos mantenido la tradición de innovar y sorprender con cada creación.

                    Hoy renovamos nuestro sistema de ventas online para que nuestros clientes disfruten de una experiencia de compra moderna, fácil y accesible, llevando la dulzura directamente a sus hogares.
                """.trimIndent()
            )

            // 🔸 Misión
            SectionNosotros(
                imagen = R.drawable.diversos_productos,
                titulo = "Misión",
                descripcion = """
                    Ofrecer una experiencia dulce y memorable a nuestros clientes, proporcionando tortas y productos de repostería de alta calidad para todas las ocasiones, mientras celebramos nuestras raíces históricas y fomentamos la creatividad en la repostería.
                """.trimIndent()
            )

            // 🔸 Visión
            SectionNosotros(
                imagen = R.drawable.estudiante_de_reposteria_aprendiendo_en_la_cocina,
                titulo = "Visión",
                descripcion = """
                    Convertirnos en la tienda online líder de productos de repostería en Chile, conocida por nuestra innovación, calidad y el impacto positivo en la comunidad, especialmente en la formación de nuevos talentos en gastronomía.
                """.trimIndent()
            )

            // 🔸 Impacto Comunitario
            SectionNosotros(
                imagen = R.drawable.persona_trabajando_en_una_cocina,
                titulo = "Impacto Comunitario",
                descripcion = """
                    Cada compra en Pastelería 1000 Sabores apoya a estudiantes de gastronomía y a la comunidad local, contribuyendo a que nuevas generaciones de reposteros sigan creando y compartiendo su arte.
                """.trimIndent()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionNosotros(imagen: Int, titulo: String, descripcion: String) {
    val marron = Color(0xFF8B4513)
    val cremaFondo = Color(0xFFFFF6E9)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(cremaFondo, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = titulo,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = titulo,
            color = marron,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = descripcion,
            color = Color(0xFF3E2E20),
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

    Spacer(modifier = Modifier.height(20.dp))
}
