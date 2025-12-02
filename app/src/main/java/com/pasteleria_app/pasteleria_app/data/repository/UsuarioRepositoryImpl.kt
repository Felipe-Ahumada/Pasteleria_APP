package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.UsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepositoryImpl @Inject constructor(
    private val dao: UsuarioDao,
    private val apiService: com.pasteleria_app.pasteleria_app.data.network.ApiService
) : UsuarioRepository {

    override suspend fun registrarUsuario(usuario: UsuarioEntity) {
        dao.registrarUsuario(usuario)
    }

    override suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? {
        return dao.obtenerUsuarioPorCorreo(correo)
    }

    // Admin
    override suspend fun getAllUsuarios(): List<com.pasteleria_app.pasteleria_app.data.model.UserResponse> {
        return apiService.getAllUsers()
    }

    override suspend fun updateUsuario(id: Long, user: com.pasteleria_app.pasteleria_app.data.model.User): com.pasteleria_app.pasteleria_app.data.model.User {
        val response = apiService.updateAdminUser(id, user)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error al actualizar usuario: Respuesta vacía")
        } else {
            throw Exception("Error al actualizar usuario: ${response.code()}")
        }
    }

    override suspend fun deactivateUsuario(id: Long) {
        val response = apiService.deleteUser(id)
        if (!response.isSuccessful) {
            throw Exception("Error al desactivar usuario: ${response.code()}")
        }
    }
}
