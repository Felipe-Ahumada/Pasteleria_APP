package com.pasteleria_app.pasteleria_app.data.model

data class Comentario(
    val id: Long? = null,
    val usuarioId: Long,
    val blogId: String,
    val contenido: String,
    val fecha: String? = null,
    val usuarioNombre: String? = null
)
