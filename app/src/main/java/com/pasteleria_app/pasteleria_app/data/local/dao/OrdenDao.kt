package com.pasteleria_app.pasteleria_app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenConItems
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenEntity
import com.pasteleria_app.pasteleria_app.data.local.entities.OrdenItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdenDao {

    @Transaction
    suspend fun insertarOrdenCompleta(orden: OrdenEntity, items: List<OrdenItemEntity>) {
        insertarOrden(orden)
        insertarItems(items)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOrden(orden: OrdenEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarItems(items: List<OrdenItemEntity>)

    @Transaction
    @Query("SELECT * FROM ordenes WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun getOrdenesPorUsuario(usuarioId: String): Flow<List<OrdenConItems>>

    @Transaction
    @Query("SELECT * FROM ordenes WHERE id = :ordenId")
    fun getOrdenPorId(ordenId: String): Flow<OrdenConItems>
}