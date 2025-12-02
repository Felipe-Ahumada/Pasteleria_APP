package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity

interface UsuarioRepository {
    suspend fun registrarUsuario(usuario: UsuarioEntity)
    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity?

    // Admin
    suspend fun getAllUsuarios(): List<com.pasteleria_app.pasteleria_app.data.model.UserResponse>
    suspend fun updateUsuario(id: Long, user: com.pasteleria_app.pasteleria_app.data.model.User): com.pasteleria_app.pasteleria_app.data.model.User
    suspend fun deactivateUsuario(id: Long)
}