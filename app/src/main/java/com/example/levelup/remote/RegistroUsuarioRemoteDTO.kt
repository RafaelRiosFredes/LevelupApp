package com.example.levelup.remote

data class RegistroUsuarioRemoteDTO(
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val contrasena: String,
    val telefono: Long,
    // Lo mandamos como String "YYYY-MM-DD" y el backend lo parsea a LocalDate
    val fechaNacimiento: String
)
