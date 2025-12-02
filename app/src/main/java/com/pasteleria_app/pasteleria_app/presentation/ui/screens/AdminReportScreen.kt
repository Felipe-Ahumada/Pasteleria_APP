package com.pasteleria_app.pasteleria_app.presentation.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.components.PasteleriaScaffold
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.AdminReportViewModel
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel

@Composable
fun AdminReportScreen(
    onOpenHome: () -> Unit,
    onOpenNosotros: () -> Unit,
    onOpenCarta: () -> Unit,
    onOpenContacto: () -> Unit,
    onOpenCarrito: () -> Unit,
    onOpenLogin: () -> Unit,
    onOpenPerfil: () -> Unit,
    onBack: () -> Unit,
    carritoViewModel: CarritoViewModel,
    viewModel: AdminReportViewModel = hiltViewModel()
) {
    val productosPorCategoria by viewModel.productosPorCategoria.collectAsState()
    val stockBajo by viewModel.stockBajo.collectAsState()
    val sinStock by viewModel.sinStock.collectAsState()
    val pedidosPorEstado by viewModel.pedidosPorEstado.collectAsState()
    val pedidosHoy by viewModel.pedidosHoy.collectAsState()
    val totalUsuarios by viewModel.totalUsuarios.collectAsState()
    val totalComentarios by viewModel.totalComentarios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    PasteleriaScaffold(
        title = "Reportes del Sistema",
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
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                        Text(
                            text = "Reportes del Sistema",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Section: Productos
                    ExpandableSection(title = "Productos") {
                        Column {
                            Text("Productos por Categoría", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            SimpleBarChart(data = productosPorCategoria)
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                StatCard("Sin Stock", sinStock.toString(), Color.Red)
                                StatCard("Stock Bajo", stockBajo.toString(), Color(0xFFFFA500)) // Orange
                            }
                        }
                    }

                    // Section: Pedidos
                    ExpandableSection(title = "Pedidos") {
                        Column {
                            Text("Pedidos por Estado", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            DonutChart(data = pedidosPorEstado)
                            Spacer(modifier = Modifier.height(16.dp))
                            StatCard("Pedidos Totales (Hoy)", pedidosHoy.toString(), MaterialTheme.colorScheme.primary)
                        }
                    }

                    // Section: Usuarios
                    ExpandableSection(title = "Usuarios") {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            StatCard("Total Usuarios", totalUsuarios.toString(), MaterialTheme.colorScheme.secondary)
                            StatCard("Comentarios", totalComentarios.toString(), Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableSection(title: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(true) }
    val rotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir",
                    modifier = Modifier.rotate(rotationState)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    content()
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        modifier = Modifier.width(150.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = label, fontSize = 14.sp, color = Color.Black)
        }
    }
}

@Composable
fun SimpleBarChart(data: Map<String, Int>) {
    if (data.isEmpty()) {
        Text("No hay datos para mostrar", color = Color.Gray)
        return
    }
    
    val maxValue = data.values.maxOrNull() ?: 0
    val barColor = MaterialTheme.colorScheme.primary

    Column(modifier = Modifier.fillMaxWidth()) {
        data.forEach { (label, value) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    modifier = Modifier.width(100.dp),
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = if (maxValue > 0) value.toFloat() / maxValue else 0f)
                            .background(barColor, RoundedCornerShape(4.dp))
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = value.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DonutChart(data: Map<String, Int>) {
    if (data.isEmpty()) {
        Text("No hay datos para mostrar", color = Color.Gray)
        return
    }

    val total = data.values.sum()
    val colors = listOf(
        Color(0xFF42A5F5), // Blue
        Color(0xFF66BB6A), // Green
        Color(0xFFFFA726), // Orange
        Color(0xFFEF5350), // Red
        Color(0xFFAB47BC)  // Purple
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Chart
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(150.dp)) {
                var startAngle = -90f
                val strokeWidth = 30.dp.toPx()

                data.entries.forEachIndexed { index, entry ->
                    val sweepAngle = (entry.value.toFloat() / total) * 360f
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    startAngle += sweepAngle
                }
            }
            Text(
                text = total.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Legend
        Column {
            data.entries.forEachIndexed { index, entry ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(colors[index % colors.size], RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${entry.key}: ${entry.value}", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
