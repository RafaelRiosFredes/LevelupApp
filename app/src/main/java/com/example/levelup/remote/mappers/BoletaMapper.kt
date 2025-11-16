package com.example.levelup.remote.mappers

import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.remote.BoletaDTO


// ENTITY → DTO
fun BoletaEntity.toDTO(): BoletaDTO =
    BoletaDTO(
        id = id,
        fecha = fecha,
        total = total,
        cantidadProductos = cantidadProductos,
        detalle = detalle
    )

// DTO → ENTITY
fun BoletaDTO.toEntity(): BoletaEntity =
    BoletaEntity(
        id = id ?: 0,
        fecha = fecha,
        total = total,
        cantidadProductos = cantidadProductos,
        detalle = detalle
    )
