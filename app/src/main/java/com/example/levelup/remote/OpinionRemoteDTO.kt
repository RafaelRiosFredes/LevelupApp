package com.example.levelup.remote

data class OpinionRemoteDTO(
    val idProducto: Long,
    val idUsuario: Long,
    val nombreUsuario: String,
    val comentario: String,
    val estrellas: Int
)
