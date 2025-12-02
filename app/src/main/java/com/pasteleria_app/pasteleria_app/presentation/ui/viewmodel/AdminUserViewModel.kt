package com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pasteleria_app.pasteleria_app.data.model.User
import com.pasteleria_app.pasteleria_app.data.model.UserResponse
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminUserViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<UserResponse>>(emptyList())
    val usuarios: StateFlow<List<UserResponse>> = _usuarios.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val listaUsuarios = usuarioRepository.getAllUsuarios()
                _usuarios.value = listaUsuarios
            } catch (e: Exception) {
                _error.value = "Error al cargar usuarios: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarUsuario(id: Long, user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                usuarioRepository.updateUsuario(id, user)
                cargarUsuarios() // Recargar lista
            } catch (e: Exception) {
                _error.value = "Error al actualizar usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun bloquearUsuario(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                usuarioRepository.deactivateUsuario(id)
                cargarUsuarios() // Recargar lista
            } catch (e: Exception) {
                _error.value = "Error al bloquear usuario: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
