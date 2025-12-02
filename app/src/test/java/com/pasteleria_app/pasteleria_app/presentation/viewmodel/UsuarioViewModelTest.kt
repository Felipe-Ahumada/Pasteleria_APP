package com.pasteleria_app.pasteleria_app.presentation.viewmodel

import com.pasteleria_app.pasteleria_app.data.model.LoginResponse
import com.pasteleria_app.pasteleria_app.data.model.UserResponse
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import com.pasteleria_app.pasteleria_app.domain.repository.AuthRepository
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.UsuarioViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UsuarioViewModelTest {

    private lateinit var viewModel: UsuarioViewModel
    private val repository: AuthRepository = mockk()
    private val prefs: UserPreferences = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UsuarioViewModel(repository, prefs)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validarUsuario success`() = runTest {
        val response = LoginResponse(token = "token", nombre = "name", correo = "email", role = "role")
        coEvery { repository.login(any()) } returns Result.success(response)
        coEvery { repository.getCurrentUser() } returns Result.success(mockk(relaxed = true))

        val result = viewModel.validarUsuario("email", "pass")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(result)
        assertTrue(viewModel.loginState.value!!.isSuccess)
    }

    @Test
    fun `validarUsuario failure`() = runTest {
        coEvery { repository.login(any()) } returns Result.failure(Exception("Error"))

        val result = viewModel.validarUsuario("email", "pass")
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(result)
        assertTrue(viewModel.loginState.value!!.isFailure)
    }

    @Test
    fun `registrarUsuario success`() = runTest {
        coEvery { repository.register(any()) } returns Result.success(mockk())

        val result = viewModel.registrarUsuario(
            "email", "pass", "name", null, "last", null, "rut", "dir"
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(result)
        assertTrue(viewModel.registerState.value!!.isSuccess)
    }

    @Test
    fun `registrarUsuario failure`() = runTest {
        coEvery { repository.register(any()) } returns Result.failure(Exception("Error"))

        val result = viewModel.registrarUsuario(
            "email", "pass", "name", null, "last", null, "rut", "dir"
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(result)
        assertTrue(viewModel.registerState.value!!.isFailure)
    }

    @Test
    fun `cerrarSesion calls prefs`() = runTest {
        viewModel.cerrarSesion()
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { prefs.clearUser() }
    }

    @Test
    fun `obtenerDatosUsuario success`() = runTest {
        val user = UserResponse(
            id = 1,
            run = "Run",
            dv = "k",
            nombre = "Name",
            apellidos = "Last",
            correo = "Email",
            fechaNacimiento = null,
            codigoDescuento = null,
            tipoUsuario = "CLIENTE",
            regionId = null,
            regionNombre = null,
            comuna = null,
            direccion = null,
            avatarUrl = null,
            activo = true
        )
        coEvery { repository.getCurrentUser() } returns Result.success(user)

        val result = viewModel.obtenerDatosUsuario()

        assertEquals("Name", result?.nombre)
    }

    @Test
    fun `obtenerDatosUsuario failure`() = runTest {
        coEvery { repository.getCurrentUser() } returns Result.failure(Exception("Error"))

        val result = viewModel.obtenerDatosUsuario()

        assertEquals(null, result)
    }

    @Test
    fun `guardarFotoPerfil calls prefs`() = runTest {
        viewModel.guardarFotoPerfil("uri")
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { prefs.saveUserPhoto("uri") }
    }
    
    @Test
    fun `validarUsuario saves user data`() = runTest {
        val response = LoginResponse(token = "token", nombre = "name", correo = "email", role = "role")
        coEvery { repository.login(any()) } returns Result.success(response)
        coEvery { repository.getCurrentUser() } returns Result.success(mockk(relaxed = true))

        viewModel.validarUsuario("email", "pass")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { prefs.saveUser("name", "email", "token", "role") }
    }
    
    @Test
    fun `validarUsuario saves user id`() = runTest {
        val response = LoginResponse(token = "token", nombre = "name", correo = "email", role = "role")
        val user = UserResponse(
            id = 123,
            run = "Run",
            dv = "k",
            nombre = "Name",
            apellidos = "Last",
            correo = "Email",
            fechaNacimiento = null,
            codigoDescuento = null,
            tipoUsuario = "CLIENTE",
            regionId = null,
            regionNombre = null,
            comuna = null,
            direccion = null,
            avatarUrl = null,
            activo = true
        )
        coEvery { repository.login(any()) } returns Result.success(response)
        coEvery { repository.getCurrentUser() } returns Result.success(user)

        viewModel.validarUsuario("email", "pass")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { prefs.saveUserId(123) }
    }
}
