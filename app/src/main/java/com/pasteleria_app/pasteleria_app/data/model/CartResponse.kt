package com.pasteleria_app.pasteleria_app.data.model

import com.google.gson.annotations.SerializedName

data class CartResponse(
    val id: Long,
    val items: List<CartItemResponse>
)
