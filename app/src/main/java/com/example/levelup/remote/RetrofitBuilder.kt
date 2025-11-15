package com.example.levelup.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    // Cuando tengas backend real, cambia esta URL:
    // private const val BASE_URL = "http://TU-IP:8080/api/"
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    // Instancia única de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ============================
    //   API DE PRODUCTOS
    // ============================
    val productosApi: ProductosApiService by lazy {
        retrofit.create(ProductosApiService::class.java)
    }

    // ============================
    //   API DE BOLETAS
    // ============================
    val boletaApi: BoletaApiService by lazy {
        retrofit.create(BoletaApiService::class.java)
    }

    // ============================
    //   API DE USUARIOS (NUEVA)
    // ============================
    val usuariosApi: UsuariosApiService by lazy {
        retrofit.create(UsuariosApiService::class.java)
    }
}
