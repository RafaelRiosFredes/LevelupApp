package com.example.levelup.remote

data class ProductoImagenCreateDTO(
    val nombreArchivo: String,
    val contentType: String,
    val base64: String
)