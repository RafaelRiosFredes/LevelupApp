package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // ID local

    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val telefono: Long? = null,
    val fechaNacimiento: String? = null,
    val fotoPerfil: ByteArray? = null,
    val duoc: Boolean = false,
    val descApl: Boolean = false,
    val rol: String = "user",
    val backendId: Long? = null
)
