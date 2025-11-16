package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos",
    indices = [
        Index(value = ["backendId"], unique = true) // optimiza sincronización
    ]
)
data class ProductosEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // ID local
    val backendId: Long? = null, // ID del backend
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val imagenUrl: String
)
