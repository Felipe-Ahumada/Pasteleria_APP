package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.UsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository

class UsuarioRepositoryImpl(
    private val dao: UsuarioDao
) : UsuarioRepository {
    override suspend fun registrarUsuario(usuario: UsuarioEntity) = dao.registrarUsuario(usuario)

    override suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? =
        dao.obtenerUsuarioPorCorreo(correo)
}
