package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductosEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String = "", // default para evitar errores al insertar
    val imagenRes: Int? = null,
    val descripcion: String
)
