package com.pasteleria_app.pasteleria_app.domain.repository

import com.pasteleria_app.pasteleria_app.data.model.LoginRequest
import com.pasteleria_app.pasteleria_app.data.model.LoginResponse
import com.pasteleria_app.pasteleria_app.data.model.User
import com.pasteleria_app.pasteleria_app.data.model.UserResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun register(user: User): Result<User>
    suspend fun getCurrentUser(): Result<UserResponse>
}
