package com.pasteleria_app.pasteleria_app.di

import android.app.Application
import androidx.room.Room
import com.pasteleria_app.pasteleria_app.data.local.database.AppDatabase
import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.repository.CarritoRepositoryImpl
import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.pasteleria_app.pasteleria_app.data.local.dao.UsuarioDao
import com.pasteleria_app.pasteleria_app.domain.repository.UsuarioRepository
import com.pasteleria_app.pasteleria_app.data.repository.UsuarioRepositoryImpl
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "pasteleria_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCarritoDao(db: AppDatabase): CarritoDao = db.carritoDao()

    @Provides
    @Singleton
    fun provideCarritoRepository(dao: CarritoDao): CarritoRepository =
        CarritoRepositoryImpl(dao)

    @Provides
    fun provideUsuarioDao(db: AppDatabase) = db.usuarioDao()

    @Provides
    @Singleton
    fun provideUsuarioRepository(dao: UsuarioDao): UsuarioRepository =
        UsuarioRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUserPreferences(app: Application): UserPreferences = UserPreferences(app)

}
