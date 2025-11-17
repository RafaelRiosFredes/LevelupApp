package com.example.levelup.remote

data class DetalleBoletaRemoteDTO(
    val idProducto: Long,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Long,
    val subtotal: Long
)
