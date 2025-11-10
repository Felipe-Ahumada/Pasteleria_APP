package com.pasteleria_app.pasteleria_app.di

import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import com.pasteleria_app.pasteleria_app.data.repository.CarritoRepositoryImpl
import com.pasteleria_app.pasteleria_app.data.repository.UsuarioRepositoryImpl
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
}
