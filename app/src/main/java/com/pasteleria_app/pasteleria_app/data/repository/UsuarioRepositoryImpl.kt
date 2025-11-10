package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.UsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepositoryImpl @Inject constructor(
    private val dao: UsuarioDao
) : UsuarioRepository {

    override suspend fun registrarUsuario(usuario: UsuarioEntity) {
        dao.registrarUsuario(usuario)
    }

    override suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? {
        return dao.obtenerUsuarioPorCorreo(correo)
    }
}
