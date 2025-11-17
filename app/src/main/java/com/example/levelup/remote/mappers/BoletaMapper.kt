package com.example.levelup.remote.mappers

import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.remote.*

// ===============================================================
// REMOTE → ENTITY (lo que recibe el backend)
// ===============================================================

fun BoletaRemoteDTO.toEntity(): BoletaEntity =
    BoletaEntity(
        backendId = idBoleta,
        total = total,
        totalSinDescuento = totalSinDescuento,
        descuentoDuocAplicado = descuentoDuocAplicado,
        descuento = descuento,
        fechaEmision = fechaEmision,
        usuarioIdBackend = idUsuario.toInt(),
        usuarioNombre = nombreUsuario.split(" ").firstOrNull(),
        usuarioApellidos = nombreUsuario.substringAfter(" ", ""),
        usuarioCorreo = "",
        detalleTexto = detalles.joinToString("\n") {
            "${it.idProducto}|${it.nombreProducto}|${it.cantidad}|${it.precioUnitario}"
        }
    )

// ===============================================================
//  ENTITY → CREATE DTO (lo que envía al backend)
// ===============================================================

fun BoletaEntity.toCreateDTO(): BoletaCreateDTO {

    val items = detalleTexto.split("\n")
        .mapNotNull { linea ->
            val p = linea.split("|")
            if (p.size < 4) return@mapNotNull null

            BoletaItemRequestDTO(
                idProducto = p[0].toLong(),
                cantidad = p[2].toInt()
            )
        }

    return BoletaCreateDTO(
        items = items,
        total = this.total,
        descuento = this.descuento ?: 0
    )
}
