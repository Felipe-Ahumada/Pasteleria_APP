package com.pasteleria_app.pasteleria_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pasteleria_app.pasteleria_app.domain.model.Producto

@Entity(tableName = "carrito")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Long, // ✅ AÑADIDO: ID del backend
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    var cantidad: Int,
    var mensaje: String? = null
) {
    fun toDomain(): Producto = Producto(
        id = id,
        productoId = productoId, // ✅ Restaurar ID
        nombre = nombre,
        precio = precio,
        imagen = imagen,
        cantidad = cantidad,
        mensaje = mensaje
    )

    companion object {
        fun fromDomain(producto: Producto) = ProductoEntity(
            id = producto.id,
            productoId = producto.productoId, // ✅ Guardar ID
            nombre = producto.nombre,
            precio = producto.precio,
            imagen = producto.imagen,
            cantidad = producto.cantidad,
            mensaje = producto.mensaje
        )
    }
}