package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class RegistroUsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val telefono: Long?,
    val fechaNacimiento: String,
    val fotoPerfil: ByteArray?,
    val duoc: Boolean = false,
    val descApl: Boolean = false,
    val rol: String = "user"
)