package com.pasteleria_app.pasteleria_app.data.local.dao

import androidx.room.*
import com.pasteleria_app.pasteleria_app.data.local.entities.CarritoUsuarioEntity

@Dao
interface CarritoUsuarioDao {
    @Query("SELECT * FROM carrito_usuario WHERE correoUsuario = :correo")
    suspend fun obtenerCarritoPorUsuario(correo: String): List<CarritoUsuarioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarProductoAlCarrito(producto: CarritoUsuarioEntity)

    @Query("DELETE FROM carrito_usuario WHERE correoUsuario = :correo")
    suspend fun vaciarCarritoUsuario(correo: String)
}
