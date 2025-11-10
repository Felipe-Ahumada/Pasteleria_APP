package com.pasteleria_app.pasteleria_app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "orden_items",
    foreignKeys = [ForeignKey(
        entity = OrdenEntity::class,
        parentColumns = ["id"],
        childColumns = ["ordenId"],
        onDelete = ForeignKey.CASCADE
    )])
data class OrdenItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ordenId: String,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Int,
    val subtotal: Int
)