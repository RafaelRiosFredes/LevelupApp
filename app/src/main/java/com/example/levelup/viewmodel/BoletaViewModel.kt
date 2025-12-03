package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.core.UserSession
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.remote.BoletaApiService
import com.example.levelup.remote.BoletaCreateDTO
import com.example.levelup.remote.BoletaItemRequestDTO
import com.example.levelup.remote.BoletaRemoteDTO
import com.example.levelup.remote.RetrofitBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BoletaViewModel (application: Application) : AndroidViewModel(application) {

    private val api : BoletaApiService = RetrofitBuilder.boletaApi
    private val _boletaActual = MutableStateFlow<BoletaRemoteDTO?>(null)
    val boletaActual: StateFlow<BoletaRemoteDTO?> = _boletaActual


    //  HISTORIAL DE BOLETAS
    private val _historial = MutableStateFlow<List<BoletaRemoteDTO>>(emptyList())
    val historial: StateFlow<List<BoletaRemoteDTO>> = _historial

    // se crea la boleta en backend a partir de carrito
    suspend fun crearBoletaBackend(itemsCarrito: List<CarritoEntity>): BoletaRemoteDTO {
        val token = UserSession.jwt?: throw IllegalStateException("Usuario no logueado")

        //mapeo de carrito
        val items = itemsCarrito.map {
            BoletaItemRequestDTO(
                idProducto = it.productoId,
                cantidad = it.cantidad
            )
        }

        //DTO que espera el backend
        val body = BoletaCreateDTO(items = items)

        //llamada al backend
        val respuesta = api.crearBoleta("Bearer $token", body)

        //guarda en StateFLow
        _boletaActual.value = respuesta

        return respuesta
    }

    fun obtenerBoletaId(id: Long) {
        val token = UserSession.jwt ?: throw IllegalStateException("Usuario no logueado")

        viewModelScope.launch {
            val resp = api.obtenerBoletaId("Bearer $token",id)
            _boletaActual.value = resp
        }
    }

    // ----------------------------------------------------
    //  OBTENER HISTORIAL DE BOLETAS (backend)
    // ----------------------------------------------------
    fun obtenerBoletas(page: Int = 0, size: Int = 20) {
        val token = UserSession.jwt ?: throw IllegalStateException("Usuario no logueado")

        viewModelScope.launch {
            try {
                val resp = api.obtenerBoletas(
                    auth = "Bearer $token",
                    page = page,
                    size = size
                )

                // PageResponseBoletaDTO.content
                _historial.value = resp.content

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
