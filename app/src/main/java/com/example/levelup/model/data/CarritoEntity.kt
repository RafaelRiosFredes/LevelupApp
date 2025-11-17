package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val productoId: Long,      // id del producto
    val nombre: String,        // nombre del producto
    val precio: Long,          // PRECIO EN LONG (no Double)
    val cantidad: Int = 1,     // cantidad
    val imagenUrl: String?     // url opcional (nullable)
)
