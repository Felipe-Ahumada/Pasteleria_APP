package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.data.model.Producto

interface ProductoRepository {
    suspend fun getProductos(): List<Producto>
    suspend fun getProducto(id: Long): Producto?
    suspend fun getProductosPorCategoria(categoriaId: Long): List<Producto>
}
