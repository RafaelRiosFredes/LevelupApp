package com.example.levelup.remote

data class LoginResponseDTO(
    val token: String,
    val username: String,
    val message: String,
    val idUsuario: Long,
    val roles: Any
)