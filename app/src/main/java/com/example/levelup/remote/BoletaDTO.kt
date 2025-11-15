package com.example.levelup.remote

data class BoletaDTO(
    val id: Long? = null,
    val fecha: Long,
    val total: Double,
    val cantidadProductos: Int,
    val detalle: String
)
