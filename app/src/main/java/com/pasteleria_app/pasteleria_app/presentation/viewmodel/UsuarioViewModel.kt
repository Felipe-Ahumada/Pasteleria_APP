package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val prefs: UserPreferences
) : ViewModel() {

    val usuarioActual = prefs.userNameFlow
    val usuarioCorreo = prefs.userEmailFlow
    val usuarioFoto = prefs.userPhotoFlow // ✅ NUEVO

    suspend fun registrarUsuario(
        correo: String,
        contrasena: String,
        nombre: String,
        apellido: String
    ): Boolean {
        val existente = repository.obtenerUsuarioPorCorreo(correo)
        if (existente != null) return false

        val nuevo = UsuarioEntity(
            correo = correo,
            contrasena = contrasena,
            nombre = nombre,
            apellido = apellido
        )
        repository.registrarUsuario(nuevo)
        return true
    }

    suspend fun validarUsuario(correo: String, contrasena: String): Boolean {
        val usuario = repository.obtenerUsuarioPorCorreo(correo)
        if (usuario != null && usuario.contrasena == contrasena) {
            prefs.saveUser(usuario.nombre, usuario.correo)
            return true
        }
        return false
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? {
        return repository.obtenerUsuarioPorCorreo(correo)
    }

    fun guardarFotoPerfil(uri: String) { // ✅ NUEVO
        viewModelScope.launch {
            prefs.saveUserPhoto(uri)
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.clearUser()
            // ❌ No borramos la foto, para mantenerla persistente
        }
    }
}
