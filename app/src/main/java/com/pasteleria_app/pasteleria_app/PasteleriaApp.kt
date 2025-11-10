package com.pasteleria_app.pasteleria_app

import android.app.Application
// --- AÑADIR IMPORTS ---
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
// ----------------------
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PasteleriaApp : Application() {

    // --- AÑADIR ESTE CÓDIGO ---
    companion object {
        const val PEDIDOS_CHANNEL_ID = "pedidos_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Solo para Android 8.0 (Oreo) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Mis Pedidos"
            val descriptionText = "Notificaciones sobre el estado de tus pedidos"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(PEDIDOS_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Registrar el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    // --- FIN DEL CÓDIGO A AÑADIR ---
}