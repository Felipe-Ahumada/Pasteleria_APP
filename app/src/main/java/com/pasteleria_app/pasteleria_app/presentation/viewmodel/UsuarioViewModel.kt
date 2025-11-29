package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.data.model.LoginRequest
import com.pasteleria_app.pasteleria_app.data.model.User
import com.pasteleria_app.pasteleria_app.domain.repository.AuthRepository
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val prefs: UserPreferences
) : ViewModel() {

    val usuarioActual = prefs.userNameFlow
    val usuarioCorreo = prefs.userEmailFlow
    val usuarioFoto = prefs.userPhotoFlow

    private val _loginState = MutableStateFlow<Result<Unit>?>(null)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Result<Unit>?>(null)
    val registerState = _registerState.asStateFlow()

    suspend fun registrarUsuario(
        correo: String,
        contrasena: String,
        primerNombre: String,
        segundoNombre: String?,
        apellidoPaterno: String,
        apellidoMaterno: String?,
        rut: String?,
        direccion: String?
    ): Boolean {
        val newUser = User(
            run = rut ?: "",
            dv = null, // Backend might handle or need split
            nombre = "$primerNombre ${segundoNombre ?: ""}".trim(),
            apellidos = "$apellidoPaterno ${apellidoMaterno ?: ""}".trim(),
            correo = correo,
            password = contrasena, // Note: User model needs password field for registration
            tipoUsuario = "CLIENTE",
            fechaNacimiento = null,
            codigoDescuento = null,
            regionId = null,
            regionNombre = null,
            comuna = null,
            direccion = direccion,
            avatarUrl = null
        )

        val result = authRepository.register(newUser)
        _registerState.value = if (result.isSuccess) Result.success(Unit) else Result.failure(result.exceptionOrNull()!!)
        return result.isSuccess
    }

    suspend fun validarUsuario(correo: String, contrasena: String): Boolean {
        val loginResult = authRepository.login(LoginRequest(correo, contrasena))
        if (loginResult.isSuccess) {
            val response = loginResult.getOrNull()!!
            // Guardar token y datos básicos
            prefs.saveUser(response.nombre, response.correo, response.token)
            
            // Obtener ID del usuario
            val userResult = authRepository.getCurrentUser()
            if (userResult.isSuccess) {
                val user = userResult.getOrNull()!!
                prefs.saveUserId(user.id)
            }
            _loginState.value = Result.success(Unit)
            return true
        } else {
            _loginState.value = Result.failure(loginResult.exceptionOrNull() ?: Exception("Login failed"))
            return false
        }
    }

    fun guardarFotoPerfil(uri: String) {
        viewModelScope.launch {
            prefs.saveUserPhoto(uri)
        }
    }

    suspend fun obtenerDatosUsuario(): com.pasteleria_app.pasteleria_app.data.model.UserResponse? {
        val result = authRepository.getCurrentUser()
        return result.getOrNull()
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.clearUser()
        }
    }
}
