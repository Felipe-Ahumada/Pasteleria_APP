package com.pasteleria_app.pasteleria_app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items") // Asumo que este es el nombre de tu tabla en Room
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // ✅ CAMBIADO a Long para compatibilidad
    val productoId: Long = 0,
    val nombre: String,
    val precio: Int,
    val imagen: Int = 0,
    val imagenUrl: String? = null,
    var cantidad: Int = 1,
    var mensaje: String? = null,
    @androidx.room.Ignore val estado: String? = "ACTIVO",
    @androidx.room.Ignore val codigoProducto: String? = "",
    @androidx.room.Ignore val descripcion: String? = null,
    @androidx.room.Ignore val stock: Int = 0,
    @androidx.room.Ignore val stockCritico: Int = 0,
    @androidx.room.Ignore val categoria: Categoria? = null
)