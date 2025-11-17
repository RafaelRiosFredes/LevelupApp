package com.example.levelup.remote

data class BoletaRemoteDTO(
    val idBoleta: Long,
    val total: Long,
    val totalSinDescuento: Long,
    val descuentoDuocAplicado: Boolean,
    val nombreUsuario: String,
    val correoUsuario: String,
    val fechaEmision: String,
    val detalles: List<DetalleBoletaRemoteDTO>
)
