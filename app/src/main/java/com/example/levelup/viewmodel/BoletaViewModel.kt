package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.core.UserSession
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.repository.BoletaRepository
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

    private val api = RetrofitBuilder.boletaApi
    private val _boletaActual = MutableStateFlow<BoletaRemoteDTO?>(null)
    val boletaActual: StateFlow<BoletaRemoteDTO?> = _boletaActual

    // se crea la boleta en backend a partir de carrito
    fun crearBoletaBackend(itemsCarrito: List<CarritoEntity>) {
        val token = UserSession.jwt?: throw IllegalStateException("Usuario no logueado")

        viewModelScope.launch {
            val items = itemsCarrito.map {
                BoletaItemRequestDTO(
                    idProducto = it.productoId,
                    cantidad = it.cantidad

                )
            }

            val body = BoletaCreateDTO(
                items = items
            )

            val respuesta = api.crearBoleta("Bearer $token", body)

            _boletaActual.value = respuesta
        }
    }

    fun obtenerBoletaId(id: Long) {
        val token = UserSession.jwt ?: throw IllegalStateException("Usuario no logueado")

        viewModelScope.launch {
            val resp = api.obtenerBoletaId("Bearer $token",id)
            _boletaActual.value = resp
        }
    }

}
