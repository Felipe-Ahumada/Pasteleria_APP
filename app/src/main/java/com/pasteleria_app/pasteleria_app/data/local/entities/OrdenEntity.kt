package com.pasteleria_app.pasteleria_app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ordenes")
data class OrdenEntity(
    @PrimaryKey val id: String, // Será algo como "PED-MHT75FAU"
    val usuarioId: String, // El correo o ID del usuario
    val trackingId: String, // Ej: "ENV-FO21GQ0H"
    val fechaCreacion: Long,
    val estado: String, // Ej: "Preparación"
    val total: Int,
    val tipoEntrega: String,
    val direccionEntrega: String, // Puede ser "Retiro en tienda" o una dirección
    val comuna: String,
    val fechaPreferida: String // Ej: "2025-11-13"
)