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
    private val carritoUsuarioDao: CarritoUsuarioDao // ✅ nuevo DAO inyectado
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    init {
        // 🧁 Observa cambios del carrito actual (sesión)
        viewModelScope.launch {
            repository.obtenerProductos().collectLatest { lista ->
                _productos.value = lista
            }
        }
    }

    // 🧩 Funciones básicas del carrito
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

    // Total: dejamos Int porque tus precios son Int
    fun calcularTotal(): Int = productos.value.sumOf { it.precio * it.cantidad }

    // Guarda snapshot del carrito de sesión en la tabla por-usuario
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
                        imagen = p.imagen // ✅ guardamos imagen real
                    )
                )
            }
        }
    }

    // Reconstruye la TABLA de sesión desde lo persistido (¡no seteamos _productos!)
    fun cargarCarritoUsuario(correo: String) {
        viewModelScope.launch {
            val guardados = carritoUsuarioDao.obtenerCarritoPorUsuario(correo)

            // 1) vaciamos tabla de carrito de sesión
            repository.vaciarCarrito()

            // 2) la repoblamos; el Flow del repository emitirá la lista completa
            guardados.forEach { item ->
                repository.agregarProducto(
                    com.pasteleria_app.pasteleria_app.domain.model.Producto(
                        id = item.productoId,
                        nombre = item.nombre,
                        precio = item.precio,
                        cantidad = item.cantidad,
                        imagen = item.imagen // ✅ restauramos imagen real
                    )
                )
            }
            // 3) NO tocar _productos directamente: lo actualiza el Flow del repository
        }
    }

}
