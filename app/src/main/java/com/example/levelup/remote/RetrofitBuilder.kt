package com.example.levelup.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit // <--- Si esta línea falta, Retrofit saldrá en rojo
import retrofit2.converter.gson.GsonConverterFactory
import com.example.levelup.core.AuthInterceptor // Tu interceptor

object RetrofitBuilder {

    const val BASE_URL = "http://10.0.2.2:9090/" // Agrega el / al final por convención

    // 1. CONFIGURAMOS EL CLIENTE CON EL INTERCEPTOR
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor()) // <--- Aquí inyectamos el token automáticamente
            .build()
    }

    // 2. INSTANCIA DE RETROFIT USANDO EL CLIENTE
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // <--- ASIGNAMOS EL CLIENTE AQUI
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
    // ========================= ===
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

    // ============================
    //   API DE OPINIONES
    // ============================
        val opinionesApi: OpinionesApiService by lazy {
            retrofit.create(OpinionesApiService::class.java)
        }

}
