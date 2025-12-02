package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.data.model.Comentario

interface ComentarioRepository {
    suspend fun getAllComentarios(): List<Comentario>
}
