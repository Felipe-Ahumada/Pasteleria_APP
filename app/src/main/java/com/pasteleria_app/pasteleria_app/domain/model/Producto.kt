package com.pasteleria_app.pasteleria_app.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val cantidad: Int = 1,
    val imagen: Int = 0
)