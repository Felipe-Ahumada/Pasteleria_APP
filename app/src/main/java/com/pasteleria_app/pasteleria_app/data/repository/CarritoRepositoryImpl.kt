package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.local.entities.ProductoEntity
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.pasteleria_app.pasteleria_app.data.model.AddToCartRequest
import javax.inject.Inject
import javax.inject.Singleton
import com.pasteleria_app.pasteleria_app.data.network.ApiService

import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Singleton
class CarritoRepositoryImpl @Inject constructor(
    private val dao: CarritoDao,
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : CarritoRepository {

    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    init {
        _refreshTrigger.tryEmit(Unit)
    }

    private suspend fun getToken(): String? {
        val token = userPreferences.userTokenFlow.first()
        return if (!token.isNullOrBlank()) "Bearer $token" else null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun obtenerProductos(): Flow<List<Producto>> = userPreferences.userTokenFlow.flatMapLatest { token ->
        if (!token.isNullOrBlank()) {
            _refreshTrigger.map {
                try {
                    val response = apiService.getCart() // Auth handled by Interceptor
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.items.map { it.toDomain() }
                    } else {
                        emptyList()
                    }
                } catch (e: Exception) {
                    emptyList() 
                }
            }
        } else {
             dao.obtenerProductos().map { lista -> lista.map { it.toDomain() } }
        }
    }

    override suspend fun agregarProducto(producto: Producto) {
        val token = getToken()
        if (token != null) {
            val request = AddToCartRequest(
                productId = producto.productoId.toString(),
                quantity = producto.cantidad,
                message = producto.mensaje ?: ""
            )
            try {
                apiService.addToCart(request)
                _refreshTrigger.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val existente = dao.obtenerProductoPorNombre(producto.nombre)
            if (existente != null) {
                val actualizado = existente.copy(
                    cantidad = existente.cantidad + producto.cantidad,
                    mensaje = producto.mensaje
                )
                dao.actualizarProducto(actualizado)
            } else {
                dao.agregarProducto(ProductoEntity.fromDomain(producto))
            }
        }
    }

    override suspend fun actualizarProducto(producto: Producto) {
        val token = getToken()
        if (token != null) {
            try {
                apiService.updateCartItem(producto.id, mapOf("quantity" to producto.cantidad))
                _refreshTrigger.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            dao.actualizarProducto(ProductoEntity.fromDomain(producto))
        }
    }

    override suspend fun eliminarProducto(producto: Producto) {
        val token = getToken()
        if (token != null) {
            try {
                apiService.removeCartItem(producto.id)
                _refreshTrigger.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            dao.eliminarProducto(ProductoEntity.fromDomain(producto))
        }
    }

    override suspend fun vaciarCarrito() {
        val token = getToken()
        if (token != null) {
            try {
                apiService.clearCart()
                _refreshTrigger.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            dao.vaciarCarrito()
        }
    }

    override suspend fun obtenerProductoPorNombre(nombre: String): Producto? {
        val token = getToken()
        return if (token != null) {
             try {
                val response = apiService.getCart()
                response.body()?.items?.map { it.toDomain() }?.find { it.nombre == nombre }
            } catch (e: Exception) {
                null
            }
        } else {
            dao.obtenerProductoPorNombre(nombre)?.toDomain()
        }
    }

    // Mapper Extension Local
    private fun com.pasteleria_app.pasteleria_app.data.model.CartItemResponse.toDomain(): Producto {
        return Producto(
            id = this.id, // CartItem ID
            productoId = this.producto.id, // Backend Product ID
            nombre = this.producto.nombre,
            precio = this.producto.precio.toInt(),
            imagen = 0, // No local resource
            imagenUrl = this.producto.imagenPrincipal,
            cantidad = this.cantidad,
            mensaje = this.mensaje
        )
    }
}