package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.OrdenDao
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenEntity
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenItemEntity
import com.pasteleria_app.pasteleria_app.domain.model.Orden
import com.pasteleria_app.pasteleria_app.domain.model.OrdenItem
import com.pasteleria_app.pasteleria_app.domain.repository.OrdenRepository
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import com.pasteleria_app.pasteleria_app.data.model.Pedido
import com.pasteleria_app.pasteleria_app.data.model.PedidoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrdenRepositoryImpl @Inject constructor(
    private val dao: OrdenDao,
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : OrdenRepository {

    override suspend fun crearOrden(orden: Orden, usuarioId: String) {
        // Obtener ID real del usuario desde Preferences (guardado al login)
        val userId = userPreferences.userIdFlow.firstOrNull() ?: throw Exception("Usuario no identificado")

        val pedido = Pedido(
            userId = userId,
            fechaEntrega = orden.fechaPreferida,
            total = orden.total.toDouble(),
            items = orden.items.map { 
                PedidoItem(
                    productoId = it.productoId, // Usar ID real del producto
                    productoNombre = it.nombreProducto,
                    cantidad = it.cantidad,
                    precioUnitario = it.precioUnitario.toDouble(),
                    subtotal = it.subtotal.toDouble(),
                    mensaje = it.mensaje
                )
            },
            direccionEntrega = orden.direccionEntrega,
            regionNombre = "Biobío",
            comuna = orden.comuna
        )
        
        val response = apiService.createPedido(pedido)
        if (!response.isSuccessful) {
            throw Exception("Error creando pedido: ${response.code()} ${response.message()}")
        }
        
        // Opcional: Guardar en local también para respaldo
        val ordenEntity = orden.toEntity(usuarioId)
        val itemsEntity = orden.items.map { it.toEntity(orden.id) }
        dao.insertarOrdenCompleta(ordenEntity, itemsEntity)
    }

    override fun getOrdenes(usuarioId: String): Flow<List<Orden>> {
        return kotlinx.coroutines.flow.flow {
            try {
                val pedidos = apiService.getMisPedidos()
                val ordenes = pedidos.map { pedido ->
                    Orden(
                        id = pedido.id?.toString() ?: "",
                        trackingId = pedido.id?.toString() ?: "",
                        fechaCreacion = try {
                            // Parse ISO date string to Long
                            val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                            format.parse(pedido.fecha ?: "")?.time ?: 0L
                        } catch (e: Exception) {
                            0L
                        },
                        estado = pedido.estado,
                        total = pedido.total.toInt(),
                        tipoEntrega = pedido.direccionEntrega?.let { "Despacho" } ?: "Retiro",
                        direccionEntrega = pedido.direccionEntrega ?: "",
                        comuna = pedido.comuna ?: "",
                        fechaPreferida = pedido.fechaEntrega ?: "",
                        items = pedido.items.map { item ->
                            OrdenItem(
                                productoId = item.productoId, // Mapear desde backend
                                nombreProducto = item.productoNombre,
                                cantidad = item.cantidad,
                                precioUnitario = item.precioUnitario.toInt(),
                                subtotal = item.subtotal.toInt(),
                                mensaje = item.mensaje
                            )
                        }
                    )
                }
                emit(ordenes)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override fun getOrden(ordenId: String): Flow<Orden> {
        return dao.getOrdenPorId(ordenId).map { it.toDomain() }
    }
}

// Mappers

fun Orden.toEntity(usuarioId: String): OrdenEntity {
    return OrdenEntity(
        id = id,
        usuarioId = usuarioId,
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

fun OrdenItem.toEntity(ordenId: String): OrdenItemEntity {
    return OrdenItemEntity(
        ordenId = ordenId,
        nombreProducto = nombreProducto,
        cantidad = cantidad,
        precioUnitario = precioUnitario,
        subtotal = subtotal,
        mensaje = mensaje // <-- AÑADIDO
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
        subtotal = subtotal,
        mensaje = mensaje // <-- AÑADIDO
    )
}