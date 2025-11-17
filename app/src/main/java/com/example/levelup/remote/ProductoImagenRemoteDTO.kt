package com.example.levelup.remote

data class ProductoImagenRemoteDTO(
    val idImagen: Long,
    val url: String,
    val contentType: String?,
    val sizeBytes: Long?,
    val nombreArchivo: String?
)
