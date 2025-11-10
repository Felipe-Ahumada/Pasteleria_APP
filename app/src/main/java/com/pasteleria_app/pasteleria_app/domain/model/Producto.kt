package com.pasteleria_app.pasteleria_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items") // Asumo que este es el nombre de tu tabla en Room
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val imagen: Int = 0,
    var cantidad: Int = 1,
    var mensaje: String? = null // <-- AÑADIDO: Para guardar "¡Feliz Cumple!"
)