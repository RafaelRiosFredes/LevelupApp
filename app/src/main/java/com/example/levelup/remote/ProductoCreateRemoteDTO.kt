package com.example.levelup.remote

data class ProductoCreateRemoteDTO(
    val nombreProducto: String,
    val descripcion: String,
    val precio: Long,
    val stock: Int,
    val categoriaId: Long
)
