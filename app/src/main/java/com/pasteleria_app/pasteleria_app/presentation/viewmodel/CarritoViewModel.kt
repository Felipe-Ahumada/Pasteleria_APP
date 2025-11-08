package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class ProductoCarrito(
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    var cantidad: Int = 1
)

class CarritoViewModel : ViewModel() {

    private val _productos = mutableStateListOf<ProductoCarrito>()
    val productos: List<ProductoCarrito> get() = _productos

    // 🛒 Agregar producto (si existe, aumenta cantidad)
    fun agregarProducto(producto: ProductoCarrito) {
        val existente = _productos.find { it.nombre == producto.nombre }
        if (existente != null) {
            existente.cantidad++
        } else {
            _productos.add(producto)
        }
    }

    // ➕ Aumentar cantidad
    fun aumentarCantidad(producto: ProductoCarrito) {
        val index = _productos.indexOfFirst { it.nombre == producto.nombre }
        if (index != -1) _productos[index] = _productos[index].copy(
            cantidad = _productos[index].cantidad + 1
        )
    }

    // ➖ Disminuir cantidad (elimina si llega a 0)
    fun disminuirCantidad(producto: ProductoCarrito) {
        val index = _productos.indexOfFirst { it.nombre == producto.nombre }
        if (index != -1) {
            val cantidadActual = _productos[index].cantidad
            if (cantidadActual > 1) {
                _productos[index] = _productos[index].copy(cantidad = cantidadActual - 1)
            } else {
                _productos.removeAt(index)
            }
        }
    }

    // ❌ Eliminar producto
    fun eliminarProducto(producto: ProductoCarrito) {
        _productos.removeAll { it.nombre == producto.nombre }
    }

    // 🧹 Vaciar carrito
    fun vaciarCarrito() {
        _productos.clear()
    }

    // 💰 Calcular total
    fun calcularTotal(): Int {
        return _productos.sumOf { it.precio * it.cantidad }
    }
}
