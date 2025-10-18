package com.example.levelup.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "productos")
data class ProductosEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val imagenRes: Int,
    val descripcion: String
)