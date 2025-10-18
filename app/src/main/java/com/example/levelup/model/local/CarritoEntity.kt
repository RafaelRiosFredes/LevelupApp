package com.example.levelup.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val imagenRes: Int,
    var cantidad: Int = 1
)