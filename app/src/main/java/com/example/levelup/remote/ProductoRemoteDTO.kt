package com.example.levelup.remote

data class ProductoRemoteDTO(
    val idProducto: Long,
    val nombreProducto: String,
    val descripcion: String,
    val precio: Long,
    val stock: Int,
    val categoriaId: Long,
    val categoriaNombre: String,
    val imagenes: List<ProductoImagenRemoteDTO>
)
