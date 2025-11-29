package com.pasteleria_app.pasteleria_app.data.repository

import com.pasteleria_app.pasteleria_app.data.model.LoginRequest
import com.pasteleria_app.pasteleria_app.data.model.LoginResponse
import com.pasteleria_app.pasteleria_app.data.model.User
import com.pasteleria_app.pasteleria_app.data.model.UserResponse
import com.pasteleria_app.pasteleria_app.data.network.ApiService
import com.pasteleria_app.pasteleria_app.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                android.util.Log.d("AuthRepo", "Login success: ${response.body()}")
                Result.success(response.body()!!)
            } else {
                android.util.Log.e("AuthRepo", "Login failed: ${response.code()} ${response.message()} ${response.errorBody()?.string()}")
                Result.failure(Exception("Login failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepo", "Login exception", e)
            Result.failure(e)
        }
    }

    override suspend fun register(user: User): Result<User> {
        return try {
            val response = apiService.register(user)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                android.util.Log.e("AuthRepo", "Register failed: ${response.code()} ${response.message()} ${response.errorBody()?.string()}")
                Result.failure(Exception("Registration failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepo", "Register exception", e)
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<UserResponse> {
        return try {
            val response = apiService.getCurrentUser()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                android.util.Log.e("AuthRepo", "GetCurrentUser failed: ${response.code()} ${response.message()} ${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to get user: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepo", "GetCurrentUser exception", e)
            Result.failure(e)
        }
    }
}
