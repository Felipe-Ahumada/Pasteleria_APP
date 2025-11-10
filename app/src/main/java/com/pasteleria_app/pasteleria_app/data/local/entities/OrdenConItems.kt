package com.pasteleria_app.pasteleria_app.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class OrdenConItems(
    @Embedded val orden: OrdenEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "ordenId"
    )
    val items: List<OrdenItemEntity>
)