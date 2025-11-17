package com.example.levelup.remote

data class UsuarioResponseRemoteDTO(
    val idUsuario: Long,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val telefono: Long?,
    val fechaNacimiento: String // viene como "YYYY-MM-DD"
)
