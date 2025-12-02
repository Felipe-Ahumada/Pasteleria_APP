package com.pasteleria_app.pasteleria_app.di

import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import com.pasteleria_app.pasteleria_app.domain.repository.AuthRepository
import com.pasteleria_app.pasteleria_app.domain.repository.ProductoRepository
import com.pasteleria_app.pasteleria_app.data.repository.CarritoRepositoryImpl
import com.pasteleria_app.pasteleria_app.data.repository.UsuarioRepositoryImpl
import com.pasteleria_app.pasteleria_app.data.repository.AuthRepositoryImpl
import com.pasteleria_app.pasteleria_app.data.repository.ProductoRepositoryImpl
import com.pasteleria_app.pasteleria_app.domain.repository.ComentarioRepository
import com.pasteleria_app.pasteleria_app.data.repository.ComentarioRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarritoRepository(
        impl: CarritoRepositoryImpl
    ): CarritoRepository

    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(
        impl: UsuarioRepositoryImpl
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton

    abstract fun bindProductoRepository(
        impl: ProductoRepositoryImpl
    ): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindComentarioRepository(
        impl: ComentarioRepositoryImpl
    ): ComentarioRepository
}