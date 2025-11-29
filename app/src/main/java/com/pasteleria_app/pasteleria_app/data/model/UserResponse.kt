package com.pasteleria_app.pasteleria_app.data.model

data class UserResponse(
    val id: Long,
    val run: String,
    val dv: String?,
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val fechaNacimiento: String?,
    val codigoDescuento: String?,
    val tipoUsuario: String,
    val regionId: String?,
    val regionNombre: String?,
    val comuna: String?,
    val direccion: String?,
    val avatarUrl: String?,
    val activo: Boolean
)
