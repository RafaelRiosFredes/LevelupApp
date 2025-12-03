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
    suspend fun crearBoletaBackend(
        itemsCarrito: List<CarritoEntity>,
        totalFinal: Long,
        descuentoAplicado: Int
    ): BoletaRemoteDTO {

        val token = UserSession.jwt ?: throw IllegalStateException("Usuario no logueado")

        // map items
        val items = itemsCarrito.map {
            BoletaItemRequestDTO(
                idProducto = it.backendId,
                cantidad = it.cantidad
            )
        }

        val body = BoletaCreateDTO(
            items = items,
            total = totalFinal,
            descuento = descuentoAplicado
        )

        val respuesta = api.crearBoleta(body)
        _boletaActual.value = respuesta

        return respuesta
    }


    // se obtiene boleta por id
    fun obtenerBoletaId(id: Long) {
        val token = UserSession.jwt ?: throw IllegalStateException("Usuario no logueado")

        viewModelScope.launch {
            val resp = api.obtenerBoletaId(id)
            _boletaActual.value = resp
        }
    }


    //  OBTENER HISTORIAL DE BOLETAS
    fun obtenerBoletas(page: Int = 0, size: Int = 20) {
        if (UserSession.jwt.isNullOrBlank())
            throw IllegalStateException("Usuario no logueado")

        viewModelScope.launch {
            try {
                val resp = api.obtenerBoletas(page, size)
                _historial.value = resp.content
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
