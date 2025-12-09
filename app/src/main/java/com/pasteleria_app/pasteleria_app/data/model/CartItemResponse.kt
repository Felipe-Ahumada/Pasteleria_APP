package com.pasteleria_app.pasteleria_app.data.model

import com.google.gson.annotations.SerializedName

data class CartItemResponse(
    val id: Long,
    @SerializedName("product")
    val producto: Producto, // Usa la clase Producto existente
    @SerializedName("quantity")
    val cantidad: Int,
    @SerializedName("message")
    val mensaje: String?
)
