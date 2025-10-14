package com.example.levelup.model.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductosEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val categoria: String,
)




