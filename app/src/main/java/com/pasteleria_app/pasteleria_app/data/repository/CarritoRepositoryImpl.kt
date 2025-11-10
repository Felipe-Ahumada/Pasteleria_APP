package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.local.entities.ProductoEntity
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarritoRepositoryImpl @Inject constructor(
    private val dao: CarritoDao
) : CarritoRepository {

    override fun obtenerProductos(): Flow<List<Producto>> =
        dao.obtenerProductos().map { lista -> lista.map { it.toDomain() } }

    // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
    override suspend fun agregarProducto(producto: Producto) {
        val existente = dao.obtenerProductoPorNombre(producto.nombre)

        if (existente != null) {
            // Si el producto ya existe, actualiza cantidad Y MENSAJE
            val actualizado = existente.copy(
                cantidad = existente.cantidad + producto.cantidad,
                mensaje = producto.mensaje // <-- ¡LA LÍNEA QUE FALTABA!
            )
            dao.actualizarProducto(actualizado)
        } else {
            // Si es nuevo, agrégalo al carrito
            dao.agregarProducto(ProductoEntity.fromDomain(producto))
        }
    }
    // --- FIN DE LA CORRECCIÓN ---

    override suspend fun actualizarProducto(producto: Producto) {
        dao.actualizarProducto(ProductoEntity.fromDomain(producto))
    }

    override suspend fun eliminarProducto(producto: Producto) {
        dao.eliminarProducto(ProductoEntity.fromDomain(producto))
    }

    override suspend fun vaciarCarrito() {
        dao.vaciarCarrito()
    }

    override suspend fun obtenerProductoPorNombre(nombre: String): Producto? {
        val entity = dao.obtenerProductoPorNombre(nombre)
        return entity?.toDomain()
    }
}