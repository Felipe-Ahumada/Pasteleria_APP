package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminProductViewModel @Inject constructor(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _productos.value = repository.getProductos()
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarProducto(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deleteProducto(id)

            if (result.isSuccess) {
                cargarProductos()
            } else {
                _error.value = "Error al eliminar: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    fun bloquearProducto(producto: Producto) {
        viewModelScope.launch {
            _isLoading.value = true
            // Asumimos que "bloquear" es cambiar el estado a "BLOQUEADO"
            val nuevoEstado = if (producto.estado == "ACTIVO") "BLOQUEADO" else "ACTIVO"

            // Mapear Domain -> Data para la API
            val productoData = com.pasteleria_app.pasteleria_app.data.model.Producto(
                id = producto.productoId,
                codigoProducto = producto.codigoProducto ?: "",
                nombre = producto.nombre,
                precio = producto.precio.toDouble(),
                descripcion = producto.descripcion,
                imagenPrincipal = producto.imagenUrl,
                imagenesDetalle = null,
                stock = producto.stock,
                stockCritico = producto.stockCritico,
                categoria = producto.categoria?.let { com.pasteleria_app.pasteleria_app.data.model.Categoria(it.id, it.nombre) },
                estado = nuevoEstado
            )

            val result = repository.updateProducto(producto.productoId, productoData)
            if (result.isSuccess) {
                cargarProductos()
            } else {
                _error.value = "Error al actualizar estado: ${result.exceptionOrNull()?.message}"
            }
            _isLoading.value = false
        }
    }

    suspend fun obtenerProductoPorId(id: Long): Producto? {
        return try {
            repository.getProducto(id)
        } catch (e: Exception) {
            null
        }
    }

    fun actualizarProducto(
        id: Long,
        nombre: String,
        precio: Int,
        imagenUrl: String,
        descripcion: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            println("AdminProductViewModel: Intentando actualizar producto $id")

            val productoActual = repository.getProducto(id)

            if (productoActual != null) {

                val productoData = com.pasteleria_app.pasteleria_app.data.model.Producto(
                    id = id,
                    codigoProducto = productoActual.codigoProducto ?: "",
                    nombre = nombre,
                    precio = precio.toDouble(),
                    descripcion = descripcion,
                    imagenPrincipal = imagenUrl,
                    imagenesDetalle = null,
                    stock = productoActual.stock,
                    stockCritico = productoActual.stockCritico,
                    categoria = productoActual.categoria?.let {
                        com.pasteleria_app.pasteleria_app.data.model.Categoria(it.id, it.nombre)
                    }
                )

                println("AdminProductViewModel: Enviando datos: $productoData")

                val result = repository.updateProducto(id, productoData)

                if (result.isSuccess) {
                    println("AdminProductViewModel: Actualización exitosa")
                    cargarProductos()
                    onSuccess()
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                    println("AdminProductViewModel: Error al actualizar: $errorMsg")
                    _error.value = "Error al actualizar: $errorMsg"
                }
            } else {
                println("AdminProductViewModel: No se encontró el producto original")
                _error.value = "No se encontró el producto original"
            }

            _isLoading.value = false
        }
    }
}
