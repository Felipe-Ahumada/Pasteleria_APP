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
    var mensaje: String? = null,
    @androidx.room.Ignore val estado: String? = "ACTIVO", // ✅ Ignorado por Room, usado en UI
    @androidx.room.Ignore val codigoProducto: String? = "", // ✅ Ignorado por Room, usado en UI
    @androidx.room.Ignore val descripcion: String? = null, // ✅ Ignorado por Room, usado en UI
    @androidx.room.Ignore val stock: Int = 0, // ✅ Ignorado por Room, usado en UI
    @androidx.room.Ignore val stockCritico: Int = 0, // ✅ Ignorado por Room, usado en UI
    @androidx.room.Ignore val categoria: Categoria? = null // ✅ Ignorado por Room, usado en UI
) {
    // Constructor secundario necesario para Room si se usa @Ignore en el primario
    constructor(id: Int, productoId: Long, nombre: String, precio: Int, imagen: Int, imagenUrl: String?, cantidad: Int, mensaje: String?) : 
        this(id, productoId, nombre, precio, imagen, imagenUrl, cantidad, mensaje, "ACTIVO", "", null, 0, 0, null)
}