package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.data.model.Producto

interface ProductoRepository {
    suspend fun getProductos(): List<com.pasteleria_app.pasteleria_app.domain.model.Producto>
    suspend fun getProducto(id: Long): com.pasteleria_app.pasteleria_app.domain.model.Producto?
    suspend fun getProductosPorCategoria(categoriaId: Long): List<com.pasteleria_app.pasteleria_app.domain.model.Producto>
    suspend fun createProducto(producto: com.pasteleria_app.pasteleria_app.data.model.Producto): Result<com.pasteleria_app.pasteleria_app.data.model.Producto> // Create/Update still use Data model for API
    suspend fun updateProducto(id: Long, producto: com.pasteleria_app.pasteleria_app.data.model.Producto): Result<com.pasteleria_app.pasteleria_app.data.model.Producto>
    suspend fun deleteProducto(id: Long): Result<Unit>
}
