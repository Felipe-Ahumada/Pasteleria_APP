package com.pasteleria_app.pasteleria_app.data.model

data class LoginResponse(
    val token: String,
    val correo: String,
    val nombre: String,
    val role: String
)
