package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoUsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.entities.CarritoUsuarioEntity
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
    private val repository: CarritoRepository,
    private val carritoUsuarioDao: CarritoUsuarioDao
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        viewModelScope.launch {
            repository.obtenerProductos().collectLatest { lista ->
                _productos.value = lista
            }
        }
    }

    // --- MODIFICADO AQUÍ ---
    fun agregarProducto(producto: Producto) {
        // El ViewModel ya no es "inteligente".
        // Simplemente le pasa el producto al Repositorio,
        // y el Repositorio se encarga de la lógica de "agregar o actualizar".
        viewModelScope.launch {
            repository.agregarProducto(producto)
        }
    }
    // --- FIN DE LA MODIFICACIÓN ---

    fun aumentarCantidad(producto: Producto) {
        viewModelScope.launch {
            val actualizado = producto.copy(cantidad = producto.cantidad + 1)
            repository.actualizarProducto(actualizado) // Llama a la función correcta
        }
    }

    fun disminuirCantidad(producto: Producto) {
        viewModelScope.launch {
            if (producto.cantidad > 1) {
                val actualizado = producto.copy(cantidad = producto.cantidad - 1)
                repository.actualizarProducto(actualizado) // Llama a la función correcta
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

    fun guardarCarritoUsuario(correo: String) {
        viewModelScope.launch {
            val lista = _productos.value
            carritoUsuarioDao.vaciarCarritoUsuario(correo)

            lista.forEach { p ->
                carritoUsuarioDao.agregarProductoAlCarrito(
                    CarritoUsuarioEntity(
                        correoUsuario = correo,
                        productoId = p.id,
                        nombre = p.nombre,
                        precio = p.precio,
                        cantidad = p.cantidad,
                        imagen = p.imagen,
                        mensaje = p.mensaje // <-- Esto ya está correcto
                    )
                )
            }
        }
    }

    fun cargarCarritoUsuario(correo: String) {
        viewModelScope.launch {
            val guardados = carritoUsuarioDao.obtenerCarritoPorUsuario(correo)

            repository.vaciarCarrito()

            guardados.forEach { item ->
                repository.agregarProducto(
                    com.pasteleria_app.pasteleria_app.domain.model.Producto(
                        id = item.productoId,
                        nombre = item.nombre,
                        precio = item.precio,
                        cantidad = item.cantidad,
                        imagen = item.imagen,
                        mensaje = item.mensaje // <-- Esto ya está correcto
                    )
                )
            }
        }
    }
}