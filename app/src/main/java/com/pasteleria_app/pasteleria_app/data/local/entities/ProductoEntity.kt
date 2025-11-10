package com.pasteleria_app.pasteleria_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pasteleria_app.pasteleria_app.domain.model.Producto

@Entity(tableName = "carrito")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    var cantidad: Int, // <-- MODIFICADO a 'var'
    var mensaje: String? = null // <-- AÑADIDO
) {
    fun toDomain(): Producto = Producto(
        id = id,
        nombre = nombre,
        precio = precio,
        imagen = imagen,
        cantidad = cantidad,
        mensaje = mensaje // <-- AÑADIDO
    )

    companion object {
        fun fromDomain(producto: Producto) = ProductoEntity(
            id = producto.id,
            nombre = producto.nombre,
            precio = producto.precio,
            imagen = producto.imagen,
            cantidad = producto.cantidad,
            mensaje = producto.mensaje // <-- AÑADIDO
        )
    }
}