package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.domain.repository.OrdenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrderViewModel @Inject constructor(
    private val ordenRepository: OrdenRepository
) : ViewModel() {

    private val _ordenes = MutableStateFlow<List<Orden>>(emptyList())
    val ordenes: StateFlow<List<Orden>> = _ordenes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarOrdenes()
    }

    fun cargarOrdenes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                ordenRepository.getAllOrdenes().collect { listaOrdenes ->
                    _ordenes.value = listaOrdenes
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar órdenes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarEstadoOrden(ordenId: String, nuevoEstado: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                ordenRepository.updateEstado(ordenId, nuevoEstado)
                cargarOrdenes() // Recargar lista para reflejar cambios
            } catch (e: Exception) {
                _error.value = "Error al actualizar estado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun getSiguienteEstado(estadoActual: String): String? {
        return when (estadoActual.lowercase()) {
            "pendiente" -> "preparando"
            "preparando" -> "en_camino"
            "en_camino" -> "entregado"
            else -> null
        }
    }
}
