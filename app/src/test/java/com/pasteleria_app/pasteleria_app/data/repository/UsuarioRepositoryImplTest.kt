package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.local.dao.UsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.entities.UsuarioEntity
import com.pasteleria_app.pasteleria_app.data.model.User
import com.pasteleria_app.pasteleria_app.data.model.UserResponse
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class UsuarioRepositoryImplTest {

    private lateinit var repository: UsuarioRepositoryImpl
    private val dao: UsuarioDao = mockk(relaxed = true)
    private val apiService: ApiService = mockk()

    @Before
    fun setup() {
        repository = UsuarioRepositoryImpl(dao, apiService)
    }

    @Test
    fun `registrarUsuario calls dao`() = runTest {
        val user = UsuarioEntity(
            id = 1, correo = "Email", contrasena = "Pass", primerNombre = "Name", apellidoPaterno = "Last"
        )
        repository.registrarUsuario(user)
        coVerify { dao.registrarUsuario(user) }
    }

    @Test
    fun `obtenerUsuarioPorCorreo calls dao`() = runTest {
        val user = UsuarioEntity(
            id = 1, correo = "Email", contrasena = "Pass", primerNombre = "Name", apellidoPaterno = "Last"
        )
        coEvery { dao.obtenerUsuarioPorCorreo("Email") } returns user

        val result = repository.obtenerUsuarioPorCorreo("Email")

        assertNotNull(result)
        assertEquals("Name", result?.primerNombre)
    }

    @Test
    fun `getAllUsuarios success`() = runTest {
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
        coEvery { apiService.getAllUsers() } returns listOf(user)

        val result = repository.getAllUsuarios()

        assertEquals(1, result.size)
    }

    @Test
    fun `updateUsuario success`() = runTest {
        val user = mockk<User>(relaxed = true)
        coEvery { apiService.updateAdminUser(1, any()) } returns Response.success(user)

        val result = repository.updateUsuario(1, user)

        assertNotNull(result)
    }

    @Test(expected = Exception::class)
    fun `updateUsuario failure`() = runTest {
        coEvery { apiService.updateAdminUser(1, any()) } returns Response.error(500, "".toResponseBody())

        repository.updateUsuario(1, mockk(relaxed = true))
    }

    @Test
    fun `deactivateUsuario success`() = runTest {
        coEvery { apiService.deleteUser(1) } returns Response.success(Unit)

        repository.deactivateUsuario(1)

        coVerify { apiService.deleteUser(1) }
    }

    @Test(expected = Exception::class)
    fun `deactivateUsuario failure`() = runTest {
        coEvery { apiService.deleteUser(1) } returns Response.error(500, "".toResponseBody())

        repository.deactivateUsuario(1)
    }
    
    @Test
    fun `getAllUsuarios empty`() = runTest {
        coEvery { apiService.getAllUsers() } returns emptyList()
        val result = repository.getAllUsuarios()
        assertEquals(0, result.size)
    }
    
    @Test
    fun `obtenerUsuarioPorCorreo not found`() = runTest {
        coEvery { dao.obtenerUsuarioPorCorreo(any()) } returns null
        val result = repository.obtenerUsuarioPorCorreo("email")
        assertEquals(null, result)
    }
    
    @Test(expected = Exception::class)
    fun `updateUsuario exception`() = runTest {
        coEvery { apiService.updateAdminUser(any(), any()) } throws Exception("Error")
        repository.updateUsuario(1, mockk(relaxed = true))
    }
}
