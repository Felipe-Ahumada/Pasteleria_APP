package com.pasteleria_app.pasteleria_app.di

import android.content.Context
import androidx.room.Room
import com.pasteleria_app.pasteleria_app.data.local.database.AppDatabase
import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoDao
import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoUsuarioDao
import com.pasteleria_app.pasteleria_app.data.local.dao.UsuarioDao
import com.pasteleria_app.pasteleria_app.data.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 🧁 Base de datos Room
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pasteleria_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    // ✅ DAOs
    @Provides fun provideUsuarioDao(db: AppDatabase): UsuarioDao = db.usuarioDao()
    @Provides fun provideCarritoDao(db: AppDatabase): CarritoDao = db.carritoDao()
    @Provides fun provideCarritoUsuarioDao(db: AppDatabase): CarritoUsuarioDao = db.carritoUsuarioDao()

    // ✅ Preferences (para sesión de usuario)
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ): UserPreferences = UserPreferences(context)
}
