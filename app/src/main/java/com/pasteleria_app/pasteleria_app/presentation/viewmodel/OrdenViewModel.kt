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

    fun cargarOrdenes() {
        viewModelScope.launch {
            val correo = userPreferences.userEmailFlow.first() ?: return@launch
            ordenRepository.getOrdenes(correo).collect {
                _ordenes.value = it
            }
        }
    }

    fun cargarOrdenPorId(ordenId: String) {
        viewModelScope.launch {
            ordenRepository.getOrden(ordenId).collect {
                _ordenSeleccionada.value = it
            }
        }
    }

    suspend fun crearOrden(
        productos: List<Producto>,
        total: Int,
        direccion: String,
        comuna: String,
        fechaEntrega: String,
        tipoEntrega: String
    ): String {

        val correo = userPreferences.userEmailFlow.first() ?: return ""
        val pedidoId = "PED-" + (100000..999999).random().toString()
        val trackingId = "ENV-" + (100000..999999).random().toString()

        // ---- MODIFICADO AQUÍ ----
        val items = productos.map {
            OrdenItem(
                productoId = it.productoId, // <-- AÑADIDO
                nombreProducto = it.nombre,
                cantidad = it.cantidad,
                precioUnitario = it.precio,
                subtotal = it.precio * it.cantidad,
                mensaje = it.mensaje
            )
        }
        // -------------------------

        val direccionFinal = if (tipoEntrega == "Retiro en tienda") "Retiro en tienda" else direccion
        val nuevaOrden = Orden(
            id = pedidoId,
            trackingId = trackingId,
            fechaCreacion = Date().time,
            estado = "Preparación",
            total = total,
            tipoEntrega = tipoEntrega,
            direccionEntrega = direccionFinal,
            comuna = comuna,
            fechaPreferida = fechaEntrega,
            items = items
        )

        ordenRepository.crearOrden(nuevaOrden, correo)

        return pedidoId
    }
}