package com.example.levelup.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
class LoginEntity {
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val correo: String,
    val contrasena: String
}