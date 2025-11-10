package com.pasteleria_app.pasteleria_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.domain.model.OrdenItem
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.OrdenRepository
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlin.random.Random

@HiltViewModel
class OrdenViewModel @Inject constructor(
    private val ordenRepository: OrdenRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _ordenes = MutableStateFlow<List<Orden>>(emptyList())
    val ordenes = _ordenes.asStateFlow()

    private val _ordenSeleccionada = MutableStateFlow<Orden?>(null)
    val ordenSeleccionada = _ordenSeleccionada.asStateFlow()

    // Carga el historial de órdenes del usuario logueado
    fun cargarOrdenes() {
        viewModelScope.launch {
            val correo = userPreferences.userEmailFlow.first() ?: return@launch
            ordenRepository.getOrdenes(correo).collect {
                _ordenes.value = it
            }
        }
    }

    // Carga una orden específica para la pantalla de detalle
    fun cargarOrdenPorId(ordenId: String) {
        viewModelScope.launch {
            ordenRepository.getOrden(ordenId).collect {
                _ordenSeleccionada.value = it
            }
        }
    }

    // Esta es la función que llamas desde EnvioScreen
    // ---- MODIFICADO: Ahora devuelve un String (el ID del pedido) ----
    suspend fun crearOrden(
        productos: List<Producto>,
        total: Int,
        direccion: String,
        comuna: String,
        fechaEntrega: String, // Formato "dd/MM/yyyy"
        tipoEntrega: String
    ): String { // <-- 1. CAMBIO EN LA FIRMA

        val correo = userPreferences.userEmailFlow.first() ?: return "" // <-- 2. CAMBIO EN EL RETURN TEMPRANO

        // 1. Generar IDs únicos (simulados)
        val pedidoId = "PED-" + (100000..999999).random().toString()
        val trackingId = "ENV-" + (100000..999999).random().toString()

        // 2. Mapear productos del carrito a OrdenItems
        val items = productos.map {
            OrdenItem(
                nombreProducto = it.nombre,
                cantidad = it.cantidad,
                precioUnitario = it.precio,
                subtotal = it.precio * it.cantidad
            )
        }

        // 3. Crear el objeto Orden
        val direccionFinal = if (tipoEntrega == "Retiro en tienda") "Retiro en tienda" else direccion

        val nuevaOrden = Orden(
            id = pedidoId,
            trackingId = trackingId,
            fechaCreacion = Date().time,
            estado = "Preparación", // Estado inicial
            total = total,
            tipoEntrega = tipoEntrega,
            direccionEntrega = direccionFinal,
            comuna = comuna,
            fechaPreferida = fechaEntrega,
            items = items
        )

        // 4. Guardar en el repositorio
        ordenRepository.crearOrden(nuevaOrden, correo)

        return pedidoId // <-- 3. DEVUELVE EL ID GENERADO
    }
}