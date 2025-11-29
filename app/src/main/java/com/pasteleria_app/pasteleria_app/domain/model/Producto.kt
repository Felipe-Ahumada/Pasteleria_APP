package com.pasteleria_app.pasteleria_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items") // Asumo que este es el nombre de tu tabla en Room
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Long = 0, // <-- AÑADIDO: ID del producto en el backend
    val nombre: String,
    val precio: Int,
    val imagen: Int = 0,
    val imagenUrl: String? = null,
    var cantidad: Int = 1,
    var mensaje: String? = null
)