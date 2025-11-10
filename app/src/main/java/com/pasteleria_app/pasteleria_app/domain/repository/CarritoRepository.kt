package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {
    fun obtenerProductos(): Flow<List<Producto>>
    suspend fun agregarProducto(producto: Producto)
    suspend fun actualizarProducto(producto: Producto) // <-- RENOMBRADO/MODIFICADO
    suspend fun eliminarProducto(producto: Producto)
    suspend fun vaciarCarrito()
    suspend fun obtenerProductoPorNombre(nombre: String): Producto? // <-- AÑADIDO
}