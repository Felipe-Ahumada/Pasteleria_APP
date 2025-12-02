package com.pasteleria_app.pasteleria_app.data.model

data class Producto(
    val id: Long,
    val codigoProducto: String,
    val nombre: String,
    val precio: Double,
    val descripcion: String?,
    val imagenPrincipal: String?,
    val imagenesDetalle: String?, // JSON string
    val stock: Int,
    val stockCritico: Int,
    val categoria: Categoria?,
    val estado: String? = "ACTIVO"
)
