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
    private val repository = BoletaRepository(RetrofitBuilder.boletaApi)

    private val _boletaActual = MutableStateFlow<BoletaEntity?>(null)
    val boletaActual: StateFlow<BoletaEntity?> = _boletaActual

    suspend fun crearBoletaBackend(
        boletaLocal: BoletaEntity,
        itemsCarrito: List<CarritoEntity>): BoletaEntity? {
        val token = UserSession.jwt?: throw IllegalStateException("Usuario no logueado")

        val items = itemsCarrito.map {
            BoletaItemRequestDTO(
                idProducto = it.productoId,
                cantidad = it.cantidad
            )
        }

        val body = BoletaCreateDTO(
            items = items,
            total = total,
            descuento = descuento
        )

        return api.crearBoleta("Bearer $token",body)
    }

    suspend fun obtenerBoletaId(id: Long): BoletaRemoteDTO {
        val token = UserSession.jwt
            ?: throw IllegalStateException("Usuario no logueado")

        return api.obtenerBoletaId("Bearer $token", id)
    }
}
