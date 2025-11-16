package com.example.levelup.remote.mappers

import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.remote.BoletaDTO
import com.example.levelup.remote.DetalleBoletaDTO
import com.example.levelup.remote.UsuarioBoletaDTO

fun BoletaEntity.toDTO(): BoletaDTO =
    BoletaDTO(
        idBoleta = backendId,
        descuento = descuento,
        total = total,
        fechaEmision = fechaEmision,
        usuario = UsuarioBoletaDTO(
            id = usuarioIdBackend,
            nombres = usuarioNombre,
            apellidos = usuarioApellidos,
            correo = usuarioCorreo
        ),
        detalles = detalleTexto.split("\n").mapNotNull { linea ->
            val partes = linea.split("|")
            if (partes.size < 4) return@mapNotNull null

            DetalleBoletaDTO(
                productoId = partes[0].toLongOrNull(),
                nombreProducto = partes[1],
                cantidad = partes[2].toIntOrNull(),
                precioUnitario = partes[3].toLongOrNull()
            )
        }
    )


fun BoletaDTO.toEntity(): BoletaEntity =
    BoletaEntity(
        backendId = idBoleta,
        total = total,
        descuento = descuento,
        fechaEmision = fechaEmision,

        usuarioIdBackend = usuario.id,
        usuarioNombre = usuario.nombres,
        usuarioApellidos = usuario.apellidos,
        usuarioCorreo = usuario.correo,

        detalleTexto = detalles.joinToString("\n") {
            "${it.productoId}|${it.nombreProducto}|${it.cantidad}|${it.precioUnitario}"
        }
    )
