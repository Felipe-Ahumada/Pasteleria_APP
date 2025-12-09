package com.pasteleria_app.pasteleria_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pasteleria_app.pasteleria_app.domain.model.Producto

@Entity(tableName = "carrito")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productoId: Long, // ✅ AÑADIDO: ID del backend
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    val imagenUrl: String? = null, // ✅ AÑADIDO: URL de imagen
    var cantidad: Int,
    var mensaje: String? = null
) {
    fun toDomain(): Producto = Producto(
        id = id,
        productoId = productoId,
        nombre = nombre,
        precio = precio,
        imagen = imagen,
        imagenUrl = imagenUrl, // ✅ Restaurar URL
        cantidad = cantidad,
        mensaje = mensaje
    )

    companion object {
        fun fromDomain(producto: Producto) = ProductoEntity(
            id = producto.id,
            productoId = producto.productoId,
            nombre = producto.nombre,
            precio = producto.precio,
            imagen = producto.imagen,
            imagenUrl = producto.imagenUrl, // ✅ Guardar URL
            cantidad = producto.cantidad,
            mensaje = producto.mensaje
        )
    }
}