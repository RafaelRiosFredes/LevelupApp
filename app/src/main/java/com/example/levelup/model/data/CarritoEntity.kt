package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val backendId: Long,      // id del producto backend
    val nombre: String,
    val precio: Long,
    val cantidad: Int = 1,
    val imagenUrl: String?     // url opcional (nullable)
)
