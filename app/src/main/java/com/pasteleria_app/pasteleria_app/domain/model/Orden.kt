package com.pasteleria_app.pasteleria_app.domain.model

data class Orden(
    val id: String,
    val trackingId: String,
    val fechaCreacion: Long,
    val estado: String,
    val total: Int,
    val tipoEntrega: String,
    val direccionEntrega: String,
    val comuna: String,
    val fechaPreferida: String,
    val items: List<OrdenItem>
)