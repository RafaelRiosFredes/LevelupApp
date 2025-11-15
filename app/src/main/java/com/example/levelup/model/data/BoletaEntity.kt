package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boletas")
data class BoletaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fecha: Long,
    val total: Double,
    val cantidadProductos: Int,
    val detalle: String
)
