package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.model.LoginResponse
import com.pasteleria_app.pasteleria_app.data.model.User
import com.pasteleria_app.pasteleria_app.data.model.UserResponse
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private val apiService: ApiService = mockk()

    @Before
    fun setup() {
        io.mockk.mockkStatic(android.util.Log::class)
        io.mockk.every { android.util.Log.d(any(), any()) } returns 0
        io.mockk.every { android.util.Log.e(any(), any()) } returns 0
        io.mockk.every { android.util.Log.e(any(), any(), any()) } returns 0
        repository = AuthRepositoryImpl(apiService)
    }

    @Test
    fun `login success`() = runTest {
        val response = LoginResponse("token", "name", "email", "role")
        coEvery { apiService.login(any()) } returns Response.success(response)

        val result = repository.login(mockk(relaxed = true))

        assertTrue(result.isSuccess)
        assertEquals("token", result.getOrNull()?.token)
    }

    @Test
    fun `login failure`() = runTest {
        coEvery { apiService.login(any()) } returns Response.error(401, "".toResponseBody())

        val result = repository.login(mockk(relaxed = true))

        assertTrue(result.isFailure)
    }

    @Test
    fun `login exception`() = runTest {
        coEvery { apiService.login(any()) } throws Exception("Network error")

        val result = repository.login(mockk(relaxed = true))

        assertTrue(result.isFailure)
    }

    @Test
    fun `register success`() = runTest {
        val user = mockk<User>(relaxed = true)
        coEvery { apiService.register(any()) } returns Response.success(user)

        val result = repository.register(user)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `register failure`() = runTest {
        coEvery { apiService.register(any()) } returns Response.error(400, "".toResponseBody())

        val result = repository.register(mockk(relaxed = true))

        assertTrue(result.isFailure)
    }

    @Test
    fun `register exception`() = runTest {
        coEvery { apiService.register(any()) } throws Exception("Network error")

        val result = repository.register(mockk(relaxed = true))

        assertTrue(result.isFailure)
    }

    @Test
    fun `getCurrentUser success`() = runTest {
        val userResponse = mockk<UserResponse>(relaxed = true)
        coEvery { apiService.getCurrentUser() } returns Response.success(userResponse)

        val result = repository.getCurrentUser()

        assertTrue(result.isSuccess)
    }

    @Test
    fun `getCurrentUser failure`() = runTest {
        coEvery { apiService.getCurrentUser() } returns Response.error(404, "".toResponseBody())

        val result = repository.getCurrentUser()

        assertTrue(result.isFailure)
    }

    @Test
    fun `getCurrentUser exception`() = runTest {
        coEvery { apiService.getCurrentUser() } throws Exception("Network error")

        val result = repository.getCurrentUser()

        assertTrue(result.isFailure)
    }
    
    @Test
    fun `login success but null body`() = runTest {
        coEvery { apiService.login(any()) } returns Response.success(null)

        val result = repository.login(mockk(relaxed = true))

        assertTrue(result.isFailure)
    }
}
