package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.model.Comentario
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import com.pasteleria_app.pasteleria_app.domain.repository.ComentarioRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComentarioRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ComentarioRepository {

    override suspend fun getAllComentarios(): List<Comentario> {
        return apiService.getAllComentarios()
    }
}
