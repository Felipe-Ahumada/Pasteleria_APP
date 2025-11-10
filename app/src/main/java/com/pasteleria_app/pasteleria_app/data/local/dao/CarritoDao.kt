package com.pasteleria_app.pasteleria_app.data.local.dao

import androidx.room.*
import com.pasteleria_app.pasteleria_app.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {

    @Query("SELECT * FROM carrito")
    fun obtenerProductos(): Flow<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarProducto(producto: ProductoEntity)

    @Update
    suspend fun actualizarProducto(producto: ProductoEntity)

    @Delete
    suspend fun eliminarProducto(producto: ProductoEntity)

    @Query("DELETE FROM carrito")
    suspend fun vaciarCarrito()
}
