package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opiniones")
data class OpinionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productoId: Long,
    val comentario: String,
    val estrellas: Int,
    val nombreUsuario: String
)
