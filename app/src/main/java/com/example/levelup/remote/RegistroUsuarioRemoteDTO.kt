package com.example.levelup.remote

data class RegistroUsuarioRemoteDTO(
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val telefono: Long,
    val fechaNacimiento: String
)
