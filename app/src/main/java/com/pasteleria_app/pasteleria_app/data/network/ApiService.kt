package com.pasteleria_app.pasteleria_app.data.network

import com.pasteleria_app.pasteleria_app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- Auth ---
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/register")
    suspend fun register(@Body user: User): Response<User>

    @GET("api/v1/auth/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    // --- Productos ---
    @GET("api/v1/productos")
    suspend fun getProductos(): List<Producto>

    @GET("api/v1/productos/{id}")
    suspend fun getProducto(@Path("id") id: Long): Response<Producto>

    @GET("api/v1/productos/categoria/{categoriaId}")
    suspend fun getProductosPorCategoria(@Path("categoriaId") categoriaId: Long): List<Producto>

    // --- Pedidos ---
    @POST("api/v1/pedidos")
    suspend fun createPedido(@Body pedido: Pedido): Response<Pedido>

    @GET("api/v1/pedidos")
    suspend fun getMisPedidos(): List<Pedido>

    @GET("api/v1/pedidos/{id}")
    suspend fun getPedido(@Path("id") id: Long): Response<Pedido>

    // --- Users ---
    @GET("api/v1/users/{id}")
    suspend fun getUser(@Path("id") id: Long): Response<UserResponse>

    @PUT("api/v1/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: User): Response<User>
}
