package com.example.levelup.remote

data class BoletaCreateRequestDTO(
    val idProducto: Long,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Long,
    val subtotal: Long
)
