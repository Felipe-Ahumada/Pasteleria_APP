package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val prefs: UserPreferences
) : ViewModel() {

    val usuarioActual = prefs.userNameFlow // 🧁 observable nombre
    val usuarioCorreo = prefs.userEmailFlow // 📧 observable correo

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
            prefs.saveUser(usuario.nombre, usuario.correo) // ✅ guardamos sesión completa
            return true
        }
        return false
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? {
        return repository.obtenerUsuarioPorCorreo(correo)
    }

    fun cerrarSesion() {
        viewModelScope.launch { prefs.clearUser() }
    }
}
