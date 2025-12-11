package com.example.levelup.remote

data class UsuarioDTO(
    val id: Long? = null,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val telefono: Long?,
    val fechaNacimiento: String?,
    val fotoPerfil: String?,
    val duoc: Boolean,
    val descApl: Boolean,
    val rol: String
)
