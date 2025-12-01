package com.example.levelup.remote

data class LoginResponseDTO(
    val token: String,
    val username: String,
    val message: String,
    val idUsuario: Long,
    val roles: Any? // Puede ser una lista de mapas o strings, lo dejamos genérico por ahora
)