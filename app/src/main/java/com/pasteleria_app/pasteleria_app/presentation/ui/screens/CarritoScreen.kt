package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    onOpenHome: () -> Unit = {},
    onOpenNosotros: () -> Unit = {},
    onOpenCarta: () -> Unit = {},
    onOpenContacto: () -> Unit = {},
    onOpenCarrito: () -> Unit = {}
) {
    val crema = MaterialTheme.colorScheme.background
    val marron = MaterialTheme.colorScheme.primary

    var carrito by remember { mutableStateOf(emptyList<String>()) } // 🛒 lista temporal

    PasteleriaScaffold(
        title = "Carrito de compras",
        onOpenHome = onOpenHome,
        onOpenNosotros = onOpenNosotros,
        onOpenCarta = onOpenCarta,
        onOpenContacto = onOpenContacto,
        onOpenCarrito = onOpenCarrito
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(crema)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 🧾 Encabezado de tabla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Producto", fontWeight = FontWeight.Bold)
                Text("Cantidad", fontWeight = FontWeight.Bold)
                Text("Precio", fontWeight = FontWeight.Bold)
            }

            Divider(thickness = 1.dp, color = Color(0xFFDDCDBA))

            // 🍩 Si el carrito está vacío
            if (carrito.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color(0xFFF5F2EC), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tu carrito está vacío",
                            fontSize = 18.sp,
                            color = Color(0xFF3E2E20),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(
                            onClick = onOpenCarta,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC49A6C)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                "Ver productos",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // 🧺 Aquí luego se mostrarán los productos del carrito
            }

            // 🧼 Botón Vaciar carrito
            Button(
                onClick = { carrito = emptyList() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = ButtonDefaults.outlinedButtonBorder,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    "Vaciar carrito",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.Bold
                )
            }

            // 💰 Resumen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Resumen", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = Color.Gray)
                        Text("$0")
                    }
                    Text("Sin descuentos aplicados", color = Color.Gray, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Envío", color = Color.Gray)
                        Text("—")
                    }

                    Divider(color = Color(0xFFE0D8C6), thickness = 1.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("$0", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { /* TODO: Continuar con envío */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC49A6C)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Definir envío y continuar",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
