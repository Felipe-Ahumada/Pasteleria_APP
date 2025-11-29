package com.pasteleria_app.pasteleria_app.data.model

data class User(
    val id: Long? = null,
    val run: String,
    val dv: String?,
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val fechaNacimiento: String?,
    val codigoDescuento: String?,
    val tipoUsuario: String, // Enum as String
    val regionId: String?,
    val regionNombre: String?,
    val comuna: String?,
    val direccion: String?,
    val avatarUrl: String?,
    val password: String? = null, // Added for registration
    val activo: Boolean = true
)
