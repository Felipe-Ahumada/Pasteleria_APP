package com.pasteleria_app.pasteleria_app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.domain.model.Producto // ✅ Usar Domain Model
import com.pasteleria_app.pasteleria_app.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val lista = repository.getProductos()
                _productos.value = lista
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarProductosPorCategoria(categoriaId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val lista = repository.getProductosPorCategoria(categoriaId)
                _productos.value = lista
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar productos de la categoría: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
