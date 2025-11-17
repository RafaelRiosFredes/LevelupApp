package com.example.levelup.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {


    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    // Instancia única de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ============================
    //   API DE AUTH / USUARIOS
    // ============================
    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
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
    //   API DE USUARIOS
    // ============================
    val usuariosApi: UsuariosApiService by lazy {
        retrofit.create(UsuariosApiService::class.java)
    }
}
