package com.example.levelup.model.repository

import com.example.levelup.model.local.ProductosDao
import com.example.levelup.model.local.ProductosEntity
import kotlinx.coroutines.flow.Flow


class ProductosRepository (private val productosDao: ProductosDao){

    suspend fun obtenerProductos(): List<ProductosEntity> {
        val productos = productosDao.obtenerProductos()
        if (productos.isEmpty()) {
            val iniciales = listOf(
                ProductosEntity(nombre = "Zapatillas de Danza", precio = 24990.0, imagenUrl = "https://picsum.photos/200?1"),
                ProductosEntity(nombre = "Polera de Entrenamiento", precio = 15990.0, imagenUrl = "https://picsum.photos/200?2"),
                ProductosEntity(nombre = "Pantalones de Baile", precio = 19990.0, imagenUrl = "https://picsum.photos/200?3"),
                ProductosEntity(nombre = "Botella de Agua", precio = 4990.0, imagenUrl = "https://picsum.photos/200?4"),
                ProductosEntity(nombre = "Toalla Deportiva", precio = 3990.0, imagenUrl = "https://picsum.photos/200?5"),
                ProductosEntity(nombre = "Bolso de Baile", precio = 29990.0, imagenUrl = "https://picsum.photos/200?6")
            )
            productosDao.insertarProductos(iniciales)
            return iniciales
        }
        return productos
    }



}