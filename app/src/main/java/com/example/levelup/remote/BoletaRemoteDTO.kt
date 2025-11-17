package com.example.levelup.remote

data class BoletaRemoteDTO(
    val idBoleta: Long,
    val fechaEmision: String,  // backend envía LocalDateTime → toString()
    val idUsuario: Long,
    val nombreUsuario: String,
    val totalSinDescuento: Long,
    val descuento: Int,
    val total: Long,
    val descuentoDuocAplicado: Boolean,
    val detalles: List<DetalleBoletaRemoteDTO>
)
