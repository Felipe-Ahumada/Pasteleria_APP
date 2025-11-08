package com.pasteleria_app.pasteleria_app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.local.entity.ProductoEntity

@Database(
    entities = [ProductoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carritoDao(): CarritoDao
}
