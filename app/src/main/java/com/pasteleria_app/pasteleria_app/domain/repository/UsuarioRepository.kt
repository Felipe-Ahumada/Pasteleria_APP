package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity

interface UsuarioRepository {
    suspend fun registrarUsuario(usuario: UsuarioEntity)
    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity?
}