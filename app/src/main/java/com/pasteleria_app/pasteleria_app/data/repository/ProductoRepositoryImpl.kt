package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.model.Producto
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import com.pasteleria_app.pasteleria_app.domain.repository.ProductoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductoRepository {

    override suspend fun getProductos(): List<Producto> {
        return try {
            apiService.getProductos()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProducto(id: Long): Producto? {
        return try {
            val response = apiService.getProducto(id)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getProductosPorCategoria(categoriaId: Long): List<Producto> {
        return try {
            apiService.getProductosPorCategoria(categoriaId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
