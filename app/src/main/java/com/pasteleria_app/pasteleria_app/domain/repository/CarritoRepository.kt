package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {
    fun obtenerProductos(): Flow<List<Producto>>
    suspend fun agregarProducto(producto: Producto)
    suspend fun actualizarCantidad(producto: Producto)
    suspend fun eliminarProducto(producto: Producto)
    suspend fun vaciarCarrito()
}
