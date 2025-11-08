package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.local.entity.ProductoEntity
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarritoRepositoryImpl(
    private val dao: CarritoDao
) : CarritoRepository {

    override fun obtenerProductos(): Flow<List<Producto>> =
        dao.obtenerProductos().map { lista -> lista.map { it.toDomain() } }

    override suspend fun agregarProducto(producto: Producto) {
        dao.agregarProducto(ProductoEntity.fromDomain(producto))
    }

    override suspend fun actualizarCantidad(producto: Producto) {
        dao.actualizarProducto(ProductoEntity.fromDomain(producto))
    }

    override suspend fun eliminarProducto(producto: Producto) {
        dao.eliminarProducto(ProductoEntity.fromDomain(producto))
    }

    override suspend fun vaciarCarrito() {
        dao.vaciarCarrito()
    }
}
