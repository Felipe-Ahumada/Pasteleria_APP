package com.pasteleria_app.pasteleria_app.data.model

data class Pedido(
    val id: Long? = null,
    val userId: Long,
    val fecha: String? = null, // Backend sends ISO string
    val fechaEntrega: String?,
    val estado: String = "PENDIENTE",
    val total: Double,
    val items: List<PedidoItem>,
    val direccionEntrega: String?,
    val regionNombre: String?,
    val comuna: String?
)
