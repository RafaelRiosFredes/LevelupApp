package com.example.levelup.remote

data class BoletaDTO(
    val idBoleta: Long? = null,
    val descuento: Int?,
    val total: Long,
    val fechaEmision: String,
    val usuario: UsuarioBoletaDTO,
    val detalles: List<DetalleBoletaDTO>
)

data class UsuarioBoletaDTO(
    val id: Int,
    val nombres: String?,
    val apellidos: String?,
    val correo: String?
)

data class DetalleBoletaDTO(
    val productoId: Long?,
    val nombreProducto: String?,
    val cantidad: Int?,
    val precioUnitario: Long?
)
