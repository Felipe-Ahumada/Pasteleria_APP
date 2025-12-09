package com.pasteleria_app.pasteleria_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_usuario")
data class CarritoUsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val correoUsuario: String,
    val productoId: Long,
    val nombre: String,
    val precio: Int,
    val cantidad: Int,
    val imagen: Int,
    val imagenUrl: String? = null, // ✅ AÑADIDO
    val mensaje: String? = null // <-- AÑADIDO
)