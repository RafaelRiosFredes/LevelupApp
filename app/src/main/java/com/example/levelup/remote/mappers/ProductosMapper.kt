package com.example.levelup.remote.mappers

import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.remote.ProductoCreateRemoteDTO
import com.example.levelup.remote.ProductoRemoteDTO
import com.example.levelup.remote.ProductoUpdateRemoteDTO

// ===============================
//  REMOTO → ENTITY (Room)
// ===============================
fun ProductoRemoteDTO.toEntity(): ProductosEntity =
    ProductosEntity(
        id = 0L,                         // Room lo autogenera
        backendId = idProducto,          // id real del backend
        nombre = nombreProducto,
        descripcion = descripcion,
        precio = precio,
        stock = stock,
        imagenUrl = imagenes.firstOrNull()?.url,
        categoriaId = categoriaId,
        categoriaNombre = categoriaNombre
    )

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
