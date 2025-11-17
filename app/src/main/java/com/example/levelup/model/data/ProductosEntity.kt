package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.levelup.remote.ProductoRemoteDTO

@Entity(
    tableName = "productos",
    indices = [
        Index(value = ["backendId"], unique = true) // optimiza sincronización
    ]
)
data class ProductosEntity(
    @PrimaryKey(autoGenerate = true)
    val idProducto: Long = 0, // ID local
    val backendId: Long? = null, // ID del backend
    val nombre: String,
    val precio: Long,
    val stock: Int,
    val descripcion: String,
    val imagenUrl: String?,
    val categoriaId: Long,
    val categoriaNombre: String
)
fun ProductoRemoteDTO.toEntity(): ProductosEntity =
    ProductosEntity(
        idProducto = idProducto.toLong(),          // si tu entity usa Int
        nombre = nombreProducto,
        descripcion = descripcion,
        precio = precio.toLong(),                  // si lo guardas como Int
        stock = stock,
        categoriaId = categoriaId.toLong(),
        categoriaNombre = categoriaNombre,
        imagenUrl = imagenes.firstOrNull()?.url   // para la lista de tarjetas
    )