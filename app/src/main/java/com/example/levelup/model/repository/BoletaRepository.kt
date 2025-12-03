package com.example.levelup.model.repository

import com.example.levelup.core.UserSession
import com.example.levelup.model.data.BoletaEntity
import com.example.levelup.model.data.BoletasDao
import com.example.levelup.model.data.CarritoEntity
import com.example.levelup.remote.BoletaApiService
import com.example.levelup.remote.BoletaCreateDTO
import com.example.levelup.remote.BoletaItemRequestDTO
import com.example.levelup.remote.mappers.toCreateDTO
import com.example.levelup.remote.mappers.toEntity
import kotlinx.coroutines.flow.Flow

class BoletaRepository(
    private val dao: BoletasDao,
    private val api: BoletaApiService
) {

    fun obtenerBoletas() = dao.obtenerBoletas()

    fun obtenerBoletaLocal(id: Int) = dao.boletaPorIdLocal(id)

    suspend fun insertarLocal(boleta: BoletaEntity): Long =
        dao.insertar(boleta)

    suspend fun insertarBoletasLocal(lista: List<BoletaEntity>) =
        dao.insertarBoletas(lista)


    // -----------------------------------------------------------
    // -------- BACKEND  (Este es el metodo que preguntas) --------
    // -----------------------------------------------------------

    suspend fun crearBoletaBackend(
        boletaLocal: BoletaEntity,
        itemsCarrito: List<CarritoEntity>
    ): BoletaEntity? = try {

        val token = UserSession.jwt ?: return null

        val request = BoletaCreateDTO(
            items = itemsCarrito.map {
                BoletaItemRequestDTO(
                    idProducto = it.productoId,
                    cantidad = it.cantidad
                )
            },
            total = boletaLocal.total,
            descuento = boletaLocal.descuento ?: 0
        )

        // Llamada al backend, igual que React
        val remota = api.crearBoleta("Bearer $token", request)

        // Mapear respuesta remota -> entidad Room
        BoletaEntity(
            id = 0,
            backendId = remota.idBoleta,
            total = remota.total,
            totalSinDescuento = remota.totalSinDescuento,
            descuentoDuocAplicado = remota.descuentoDuocAplicado,
            descuento = remota.descuento,
            fechaEmision = remota.fechaEmision,
            usuarioIdBackend = remota.idUsuario,
            usuarioNombre = boletaLocal.usuarioNombre,
            usuarioApellidos = boletaLocal.usuarioApellidos,
            usuarioCorreo = boletaLocal.usuarioCorreo,
            detalleTexto = boletaLocal.detalleTexto
        )

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    // -------- Obtener todas las boletas del backend --------

    suspend fun obtenerBoletasBackend() {
        try {
            val page = api.obtenerBoletas()
            val entidades = page.content.map { it.toEntity() }
            dao.eliminarTodas()
            dao.insertarBoletas(entidades)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun obtenerBoletaBackendPorId(id: Long): BoletaEntity? = try{
            val token = UserSession.jwt ?: return null
            val dto = api.obtenerBoletaId("Bearer $token", id)
            dto.toEntity()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}