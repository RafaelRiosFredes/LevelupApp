package com.example.levelup.remote

data class DetalleBoletaRemoteDTO(
    val idProducto: Long,
    val nombreProducto: String,
    val precioUnitario: Long,
    val cantidad: Int,
    val subtotal: Long
)
