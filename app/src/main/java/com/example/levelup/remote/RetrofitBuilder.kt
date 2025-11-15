package com.example.levelup.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    // Cuando tengas backend real: "http://TU-IP:8080/api/"

    //  Instancia única de Retrofit para toda la aplicación
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Productos API
    val productosApi: ProductosApiService by lazy {
        retrofit.create(ProductosApiService::class.java)
    }

    val boletaApi: BoletaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BoletaApiService::class.java)
    }

}
