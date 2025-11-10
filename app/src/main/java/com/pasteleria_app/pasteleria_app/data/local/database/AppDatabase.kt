package com.pasteleria_app.pasteleria_app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoUsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.dao.UsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.entities.ProductoEntity
import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity
import com.pasteleria_app.pasteleria_app.data.local.entities.CarritoUsuarioEntity // 👈 agrega esto

@Database(
    entities = [ProductoEntity::class, UsuarioEntity::class, CarritoUsuarioEntity::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carritoDao(): CarritoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun carritoUsuarioDao(): CarritoUsuarioDao // ✅ necesario
}

