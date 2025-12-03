package com.example.levelup.remote

data class BoletaCreateDTO(
    val items: List<BoletaItemRequestDTO>,
    val total: Long,
    val descuento: Int?
)
