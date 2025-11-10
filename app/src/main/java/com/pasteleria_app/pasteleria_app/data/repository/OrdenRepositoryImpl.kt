package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.OrdenDao
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenEntity
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenItemEntity
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.domain.model.OrdenItem
import com.pasteleria_app.pasteleria_app.domain.repository.OrdenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Añade @Inject constructor si Hilt lo maneja
class OrdenRepositoryImpl @Inject constructor(private val dao: OrdenDao) : OrdenRepository {

    // ---- MODIFICADO ----
    override suspend fun crearOrden(orden: Orden, usuarioId: String) {
        val ordenEntity = orden.toEntity(usuarioId) // Pasamos el ID aquí
        val itemsEntity = orden.items.map { it.toEntity(orden.id) }
        dao.insertarOrdenCompleta(ordenEntity, itemsEntity)
    }
    // ----

    override fun getOrdenes(usuarioId: String): Flow<List<Orden>> {
        return dao.getOrdenesPorUsuario(usuarioId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getOrden(ordenId: String): Flow<Orden> {
        return dao.getOrdenPorId(ordenId).map { it.toDomain() }
    }
}

// Mappers

// ---- MODIFICADO ----
fun Orden.toEntity(usuarioId: String): OrdenEntity {
    return OrdenEntity(
        id = id,
        usuarioId = usuarioId, // Se asigna aquí
        trackingId = trackingId,
        fechaCreacion = fechaCreacion,
        estado = estado,
        total = total,
        tipoEntrega = tipoEntrega,
        direccionEntrega = direccionEntrega,
        comuna = comuna,
        fechaPreferida = fechaPreferida
    )
}
// ----

fun OrdenItem.toEntity(ordenId: String): OrdenItemEntity {
    return OrdenItemEntity(
        ordenId = ordenId,
        nombreProducto = nombreProducto,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        subtotal = subtotal
    )
}

fun com.pasteleria_app.pasteleria_app.data.local.entities.OrdenConItems.toDomain(): Orden {
    return Orden(
        id = orden.id,
        trackingId = orden.trackingId,
        fechaCreacion = orden.fechaCreacion,
        estado = orden.estado,
        total = orden.total,
        tipoEntrega = orden.tipoEntrega,
        direccionEntrega = orden.direccionEntrega,
        comuna = orden.comuna,
        fechaPreferida = orden.fechaPreferida,
        items = items.map { it.toDomain() }
    )
}

fun OrdenItemEntity.toDomain(): OrdenItem {
    return OrdenItem(
        nombreProducto = nombreProducto,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        subtotal = subtotal
    )
}