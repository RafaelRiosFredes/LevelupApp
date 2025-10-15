package com.example.levelup.model.remote

import retrofit2.http.GET

// Esta interfaz simula un punto para futuras llamadas a APIs REST.
// Puedes implementar Retrofit aquí si luego sincronizas los gastos con un servidor.

data class Productos(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String
)

interface ProductosApiService {
    @GET("productos")
    suspend fun obtenerProductos(): List<Productos>


    // suspend fun obtenerProductosRemotos(): List<ProductosDto>
    // suspend fun enviarProductos(productos: ProductosDto)
}