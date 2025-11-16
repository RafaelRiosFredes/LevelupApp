package com.example.levelup.remote.mappers

import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.remote.ProductosDTO

// DTO → ENTITY
fun ProductosDTO.toEntity(): ProductosEntity {
    return ProductosEntity(
        id = 0,                    // Room genera id local
        backendId = this.id,       // ID REAL del backend
        nombre = this.nombre,
        precio = this.precio,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl
    )
}

// ENTITY → DTO
fun ProductosEntity.toDTO(): ProductosDTO {
    return ProductosDTO(
        id = this.backendId ?: 0,  // si el backend no ha asignado id
        nombre = this.nombre,
        precio = this.precio,
        descripcion = this.descripcion,
        imagenUrl = this.imagenUrl
    )
}
