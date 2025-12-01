package com.example.levelup.remote.mappers

import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.remote.ProductoCreateRemoteDTO
import com.example.levelup.remote.ProductoRemoteDTO
import com.example.levelup.remote.ProductoUpdateRemoteDTO
import com.example.levelup.remote.RetrofitBuilder

// ===============================
//  REMOTO → ENTITY (Room)
// ===============================
    fun ProductoRemoteDTO.toEntity(): ProductosEntity {

    // Construir URL absoluta para Coil (muy importante)
    val base = RetrofitBuilder.BASE_URL.removeSuffix("/")

    val fullImageUrl = imagenes.firstOrNull()?.url?.let { relative ->
        if (relative.startsWith("http")) relative else base + relative
    }

    return ProductosEntity(
        id = 0L,                       // Room autogenera
        backendId = idProducto,        // ID real del backend
        nombre = nombreProducto,
        descripcion = descripcion,
        precio = precio,
        stock = stock,
        imagenUrl = fullImageUrl,      // URL completa
        categoriaId = categoriaId,
        categoriaNombre = categoriaNombre
    )
}


// ===============================
//  ENTITY → DTO CREATE
// ===============================
fun ProductosEntity.toCreateRemote(): ProductoCreateRemoteDTO =
    ProductoCreateRemoteDTO(
        nombreProducto = nombre,
        descripcion = descripcion,
        precio = precio,
        stock = stock,
        categoriaId = categoriaId
    )

// ===============================
//  ENTITY → DTO UPDATE
// ===============================
fun ProductosEntity.toUpdateRemote(): ProductoUpdateRemoteDTO =
    ProductoUpdateRemoteDTO(
        nombreProducto = nombre,
        descripcion = descripcion,
        precio = precio,
        stock = stock,
        categoriaId = categoriaId
    )
