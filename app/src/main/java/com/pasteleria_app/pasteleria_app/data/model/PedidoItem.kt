package com.pasteleria_app.pasteleria_app.data.model

data class PedidoItem(
    val id: Long? = null,
    val productoId: Long,
    val productoNombre: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double,
    val mensaje: String?
)
