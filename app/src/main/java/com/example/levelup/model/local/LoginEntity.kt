package com.example.levelup.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class LoginEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val correo: String,
    val contrasena: String,
    val puntos: Int = 0
)