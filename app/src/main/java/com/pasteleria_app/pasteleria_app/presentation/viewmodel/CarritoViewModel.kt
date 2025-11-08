package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val repository: CarritoRepository
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        // Observa cambios en la base de datos en tiempo real
        viewModelScope.launch {
            repository.obtenerProductos().collectLatest {
                _productos.value = it
            }
        }
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch { repository.agregarProducto(producto) }
    }

    fun aumentarCantidad(producto: Producto) {
        viewModelScope.launch {
            val actualizado = producto.copy(cantidad = producto.cantidad + 1)
            repository.actualizarCantidad(actualizado)
        }
    }

    fun disminuirCantidad(producto: Producto) {
        viewModelScope.launch {
            if (producto.cantidad > 1) {
                val actualizado = producto.copy(cantidad = producto.cantidad - 1)
                repository.actualizarCantidad(actualizado)
            } else {
                repository.eliminarProducto(producto)
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch { repository.eliminarProducto(producto) }
    }

    fun vaciarCarrito() {
        viewModelScope.launch { repository.vaciarCarrito() }
    }

    fun calcularTotal(): Int = productos.value.sumOf { it.precio * it.cantidad }
}
