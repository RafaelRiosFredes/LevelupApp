package com.example.levelup.remote

data class ProductoUpdateRemoteDTO(
    val nombreProducto: String,
    val descripcion: String,
    val precio: Long,
    val stock: Int,
    val categoriaId: Long
)
