package com.example.levelup.model.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "productos",
    indices = [
        Index(value = ["backendId"], unique = true) // para sincronizar por id del backend
    ]
)
data class ProductosEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,                // id LOCAL de Room

    val backendId: Long? = null,      // idProducto del backend

    val nombre: String,
    val descripcion: String,
    val precio: Long,
    val stock: Int,

    val imagenUrl: String?,           // url de la primera imagen (o null)

    val categoriaId: Long,
    val categoriaNombre: String
)
