package com.pasteleria_app.pasteleria_app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pasteleria_app.pasteleria_app.PasteleriaApp
import com.pasteleria_app.pasteleria_app.R

object NotificationHelper {

    fun showOrderConfirmationNotification(
        context: Context,
        orderId: String,
        total: Int
    ) {
        // Usamos el ícono de la app (visto en tu CarritoScreen)
        val icon = R.drawable.logo_landing

        val builder = NotificationCompat.Builder(context, PasteleriaApp.PEDIDOS_CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle("¡Pedido Confirmado!")
            .setContentText("Tu pedido $orderId por ${formateaCLP(total)} está en preparación.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // La notificación desaparece al tocarla

        // Revisar si tenemos permiso (importante para Android 13+)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no tenemos permiso, no hacemos nada.
            // El permiso se pide en la EnvioScreen.
            return
        }

        // Mostrar la notificación
        with(NotificationManagerCompat.from(context)) {
            // notificationId es un ID único para esta notificación
            notify(orderId.hashCode(), builder.build())
        }
    }
}