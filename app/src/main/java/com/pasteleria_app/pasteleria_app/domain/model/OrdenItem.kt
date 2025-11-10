package com.pasteleria_app.pasteleria_app.domain.model

data class OrdenItem(
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Int,
    val subtotal: Int,
    val mensaje: String? = null // <-- AÑADIDO
)