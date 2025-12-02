package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.domain.model.Orden
import kotlinx.coroutines.flow.Flow

interface OrdenRepository {
    // ---- MODIFICADO ----
    suspend fun crearOrden(orden: Orden, usuarioId: String)
    // ----
    fun getOrdenes(usuarioId: String): Flow<List<Orden>>
    fun getOrden(ordenId: String): Flow<Orden>
    
    // Admin
    fun getAllOrdenes(): Flow<List<Orden>>
    suspend fun updateEstado(ordenId: String, nuevoEstado: String)
}