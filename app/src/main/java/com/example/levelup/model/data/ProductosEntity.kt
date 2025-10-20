package com.example.levelup.model.data


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "productos")
data class ProductosEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // ✅ Room manejará la generación automática
    val nombre: String,
    val precio: Double,
    val imagenRes: Int,
    val descripcion: String
)