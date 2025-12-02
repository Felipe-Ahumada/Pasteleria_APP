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

    override suspend fun getProductos(): List<com.pasteleria_app.pasteleria_app.domain.model.Producto> {
        return try {
            apiService.getProductos().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProducto(id: Long): com.pasteleria_app.pasteleria_app.domain.model.Producto? {
        return try {
            val response = apiService.getProducto(id)
            if (response.isSuccessful) response.body()?.toDomain() else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getProductosPorCategoria(categoriaId: Long):
            List<com.pasteleria_app.pasteleria_app.domain.model.Producto> {

        return try {
            apiService.getProductosPorCategoria(categoriaId).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // --------------------------
    // MAPEO DATA -> DOMAIN
    // --------------------------
    private fun Producto.toDomain(): com.pasteleria_app.pasteleria_app.domain.model.Producto {
        return com.pasteleria_app.pasteleria_app.domain.model.Producto(
            id = 0,
            productoId = this.id,
            nombre = this.nombre,
            precio = this.precio.toInt(),
            imagen = 0,
            imagenUrl = this.imagenPrincipal,
            cantidad = 1,
            mensaje = null,
            codigoProducto = this.codigoProducto,
            descripcion = this.descripcion,
            stock = this.stock,
            stockCritico = this.stockCritico,
            categoria = this.categoria?.let {
                com.pasteleria_app.pasteleria_app.domain.model.Categoria(it.id, it.nombre)
            },
            estado = this.estado ?: "ACTIVO"
        )
    }

    // --------------------------
    // API CREATE
    // --------------------------
    override suspend fun createProducto(producto: Producto): Result<Producto> {
        return try {
            val response = apiService.createProducto(producto)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear producto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------------
    // API UPDATE
    // --------------------------
    override suspend fun updateProducto(id: Long, producto: Producto): Result<Producto> {
        return try {
            val response = apiService.updateProducto(id, producto)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Error al actualizar producto: Code=${response.code()}, Message=${response.message()}, Body=$errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------------
    // API DELETE
    // --------------------------
    override suspend fun deleteProducto(id: Long): Result<Unit> {
        return try {
            apiService.deleteProducto(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
