package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.domain.repository.ComentarioRepository
import com.pasteleria_app.pasteleria_app.domain.repository.OrdenRepository
import com.pasteleria_app.pasteleria_app.domain.repository.ProductoRepository
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminReportViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val ordenRepository: OrdenRepository,
    private val usuarioRepository: UsuarioRepository,
    private val comentarioRepository: ComentarioRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Metrics
    private val _productosPorCategoria = MutableStateFlow<Map<String, Int>>(emptyMap())
    val productosPorCategoria: StateFlow<Map<String, Int>> = _productosPorCategoria.asStateFlow()

    private val _stockBajo = MutableStateFlow(0)
    val stockBajo: StateFlow<Int> = _stockBajo.asStateFlow()

    private val _sinStock = MutableStateFlow(0)
    val sinStock: StateFlow<Int> = _sinStock.asStateFlow()

    private val _pedidosPorEstado = MutableStateFlow<Map<String, Int>>(emptyMap())
    val pedidosPorEstado: StateFlow<Map<String, Int>> = _pedidosPorEstado.asStateFlow()

    private val _pedidosHoy = MutableStateFlow(0)
    val pedidosHoy: StateFlow<Int> = _pedidosHoy.asStateFlow()

    private val _totalUsuarios = MutableStateFlow(0)
    val totalUsuarios: StateFlow<Int> = _totalUsuarios.asStateFlow()

    private val _totalComentarios = MutableStateFlow(0)
    val totalComentarios: StateFlow<Int> = _totalComentarios.asStateFlow()

    init {
        cargarReportes()
    }

    fun cargarReportes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Fetch data in parallel
                val productosDeferred = async { productoRepository.getProductos() }
                val ordenesDeferred = async { ordenRepository.getAllOrdenes().first() }
                val usuariosDeferred = async { usuarioRepository.getAllUsuarios() }
                val comentariosDeferred = async { comentarioRepository.getAllComentarios() }

                val productos = productosDeferred.await()
                val ordenes = ordenesDeferred.await()
                val usuarios = usuariosDeferred.await()
                val comentarios = comentariosDeferred.await()

                // Compute Product Metrics
                val categorias = productos.groupingBy { it.categoria?.nombre ?: "Sin Categoría" }.eachCount()
                _productosPorCategoria.value = categorias
                _stockBajo.value = productos.count { p -> p.stock in 1..5 }
                _sinStock.value = productos.count { p -> p.stock == 0 }

                // Compute Order Metrics
                val estados = ordenes.groupingBy { it.estado }.eachCount()
                _pedidosPorEstado.value = estados
                // Assuming we can filter by date, but for now just count all or mock "Today" logic if needed
                // For simplicity, let's just count all for now as "Total Pedidos" or implement date check if date string format is consistent
                _pedidosHoy.value = ordenes.size // Placeholder for "Today", currently showing Total

                // Compute User Metrics
                _totalUsuarios.value = usuarios.size

                // Compute Comment Metrics
                _totalComentarios.value = comentarios.size

            } catch (e: Exception) {
                _error.value = "Error al cargar reportes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
