package com.pasteleria_app.pasteleria_app.domain.model

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    val cantidad: Int = 1
)